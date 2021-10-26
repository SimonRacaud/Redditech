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

        this.loadContent(myView, "rising")
        // Filters selector creation
        this.createFilterSelector(myView)

        return myView
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
                    
                    loadContent(myView, postFilter[position])
                } else {
                    loadContent(myView, "rising")
                }
            }
        }
    }

    private fun loadContent(view: View, filter: String)
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
            //TODO use it
        })
        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                //TODO: SHOW PROGRESS BAR
            } else {
                //TODO: mask progress
                recyclerView.adapter = PostListAdapter(this.context, postList, R.layout.home_tab_post_item)
            }
        })
        viewModel.getPostsFeed(filter)
    }
}
