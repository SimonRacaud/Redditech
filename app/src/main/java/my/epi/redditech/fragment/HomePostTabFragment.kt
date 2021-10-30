package my.epi.redditech.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.PostListAdapter
import my.epi.redditech.adapter.SubredditListAdapter
import my.epi.redditech.model.PostItemModel
import my.epi.redditech.model.SubredditItemModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.utils.ErrorMessage
import my.epi.redditech.viewmodel.HomePostsViewModel
import my.epi.redditech.viewmodel.HomeSubredditsViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

/**
 * Home Posts list tab
 */
class HomePostTabFragment : Fragment() {

    private lateinit var viewModel: HomePostsViewModel
    private var postFilter = arrayOf("rising", "hot", "new", "top")
    private lateinit var myView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.home_post_tab_fragment, container, false)

        this.loadContent(myView, "rising", false)
        this.handleInfiniteScroll(myView)
        // Filters selector creation
        this.createFilterSelector(myView)

        return myView
    }

    private fun handleInfiniteScroll(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d("SUBREDDITACTIVITY", "PAGE HOME CAN SCROLL")
                }
            }
        })
    }

    private fun createFilterSelector(view: View)
    {
        val spinner: Spinner = view.findViewById(R.id.filter_selector)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (postFilter[position].isNotEmpty()) {
                    
                    loadContent(myView, postFilter[position], false)
                } else {
                    loadContent(myView, "rising", false)
                }
            }
        }
    }

    private fun loadContent(view: View, filter: String, reset: Boolean)
    {
        val postList = arrayListOf<PostItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        viewModel = ViewModelProvider(this, factory).get(HomePostsViewModel::class.java)
        viewModel.postsList.observe(viewLifecycleOwner, {
            postList.clear()
            it.data.children.forEach { element ->
                postList.add(PostItemModel(element.data))
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            this.parentFragment?.activity?.let { it1 -> ErrorMessage.show(it1, it) }
        })
        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                // SHOW PROGRESS BAR
            } else {
                // mask progress
                recyclerView.adapter = PostListAdapter(this.context, postList, R.layout.home_tab_post_item)
            }
        })
        viewModel.getPostsFeed(filter)
    }
}
