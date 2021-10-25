package my.epi.redditech.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_post_tab_fragment, container, false)

        this.loadContent(view)
        // Filters selector creation
        this.createFilterSelector(view)

        return view
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
    }

    private fun loadContent(view: View)
    {
        val postList = arrayListOf<PostItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        viewModel = ViewModelProvider(this, factory).get(HomePostsViewModel::class.java)
        viewModel.postsList.observe(viewLifecycleOwner, {
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
        viewModel.getPostsFeed("new")
    }
}
