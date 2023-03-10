package my.epi.redditech.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import my.epi.redditech.R
import my.epi.redditech.adapter.SubredditListAdapter
import my.epi.redditech.model.SubredditItemModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.viewmodel.HomeSubredditsViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory
import androidx.recyclerview.widget.SimpleItemAnimator
import my.epi.redditech.adapter.PostListAdapter
import my.epi.redditech.model.PostItemModel
import my.epi.redditech.utils.ErrorMessage


/**
 * Home My Subreddits list tab
 */
class HomeSubredditsTabFragment : Fragment() {

    private lateinit var viewModel: HomeSubredditsViewModel
    private var next: String? = ""

    override fun onStart() {
        super.onStart()

        this.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_subs_tab_fragment, container, false)

        this.handleInfiniteScroll(view)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter =
            SubredditListAdapter(this.requireContext(), arrayListOf<SubredditItemModel>(), R.layout.home_tab_subreddit_item)

        return view;
    }

    /**
     * For pagination : detect scroll ending
     */
    private fun handleInfiniteScroll(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && recyclerView.computeVerticalScrollOffset() > 0) {
                    fetchData()
                }
            }
        })
    }

    /**
     * Fetch list data (network)
     */
    private fun fetchData() {
        val subList = arrayListOf<SubredditItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)

        viewModel = ViewModelProvider(this, factory).get(HomeSubredditsViewModel::class.java)
        viewModel.subredditList.observe(viewLifecycleOwner, {
            subList.clear()
            if (next != it.data.after.toString() && next != null) {
                next = it.data.after.toString()
                it.data.children.forEach { element ->
                    element.data.community_icon =
                        element.data.community_icon.toString().replace("&amp;", "&")
                    if (element.data.community_icon.toString().isNotEmpty()) {
                        subList.add(
                            SubredditItemModel(
                                element.data.display_name_prefixed,
                                element.data.community_icon,
                                element.data.subscribers
                            )
                        )
                    } else {
                        subList.add(
                            SubredditItemModel(
                                element.data.display_name_prefixed,
                                element.data.icon_img,
                                element.data.subscribers
                            )
                        )
                    }
                }
                (recyclerView?.adapter as SubredditListAdapter).append(subList)
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            this.parentFragment?.activity?.let { it1 -> ErrorMessage.show(it1, it) }
        })
        viewModel.getSubscribedSubreddit(this.next!!)
    }
}
