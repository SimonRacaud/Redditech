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
import my.epi.redditech.utils.ErrorMessage


/**
 * Home My Subreddits list tab
 */
class HomeSubredditsTabFragment : Fragment() {

    private lateinit var viewModel: HomeSubredditsViewModel

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

        return view;
    }

    private fun fetchData() {
        val subList = arrayListOf<SubredditItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)

        viewModel = ViewModelProvider(this, factory).get(HomeSubredditsViewModel::class.java)
        viewModel.subredditList.observe(viewLifecycleOwner, {
            subList.clear()
            it.data.children.forEach { element ->
                element.data.community_icon = element.data.community_icon.toString().replace("&amp;","&")
                if (element.data.community_icon.toString().isNotEmpty())
                    subList.add(SubredditItemModel(element.data.display_name_prefixed, element.data.community_icon, element.data.subscribers))
                else
                    subList.add(SubredditItemModel(element.data.display_name_prefixed, element.data.icon_img, element.data.subscribers))
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
                recyclerView?.adapter = SubredditListAdapter(this.requireContext(), subList, R.layout.home_tab_subreddit_item)
            }
        })
        viewModel.getSubscribedSubreddit()
    }
}
