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
    private var postFilter = arrayOf("hot", "new", "top", "rising")
    private lateinit var myView: View
    private var nextPost: String? = ""
    private var filterPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.home_post_tab_fragment, container, false)

        this.handleInfiniteScroll(myView)
        // Filters selector creation
        this.createFilterSelector(myView)

        val recyclerView = myView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter =
            PostListAdapter(context, arrayListOf<PostItemModel>(), R.layout.home_tab_post_item)

        return myView
    }

    /**
     * For pagination : detect scroll ending
     */
    private fun handleInfiniteScroll(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadContent(view, postFilter[filterPosition], false)
                }
            }
        })
    }

    /**
     * Init feed filter
     */
    private fun createFilterSelector(view: View) {
        val spinner: Spinner = view.findViewById(R.id.filter_selector)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterPosition = position
                if (postFilter[filterPosition].isNotEmpty()) {
                    nextPost = ""
                    loadContent(myView, postFilter[filterPosition], true)
                } else {
                    nextPost = ""
                    loadContent(myView, "hot", true)
                }
            }
        }
    }

    /**
     * Load feed content
     */
    private fun loadContent(view: View, filter: String, reset: Boolean) {
        val postList = arrayListOf<PostItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        if (reset) {
            (recyclerView.adapter as PostListAdapter).clear()
        }
        viewModel = ViewModelProvider(this, factory).get(HomePostsViewModel::class.java)
        viewModel.postsList.observe(viewLifecycleOwner, {
            postList.clear()
            if (nextPost != it.data.after.toString() && nextPost != null) {
                nextPost = it.data.after.toString()
                it.data.children.forEach { element ->
                    postList.add(PostItemModel(element.data))
                }
                (recyclerView.adapter as PostListAdapter).append(postList)
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            this.parentFragment?.activity?.let { it1 -> ErrorMessage.show(it1, it) }
        })
        viewModel.getPostsFeed(filter, nextPost!!)
    }
}
