package my.epi.redditech.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.SubredditListAdapter
import my.epi.redditech.model.SubredditItemModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.viewmodel.HomeSubredditsViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

/**
 * Home My Subreddits list tab
 */
class HomeSubredditsTabFragment : Fragment() {

    private lateinit var viewModel: HomeSubredditsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_subs_tab_fragment, container, false)

        // TODO : debug data (api call)
        val subList = arrayListOf<SubredditItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        viewModel = ViewModelProvider(this, factory).get(HomeSubredditsViewModel::class.java)
        viewModel.subredditList.observe(viewLifecycleOwner, {
            it.data.children.forEach { element ->
                element.data.community_icon = element.data.community_icon.toString().replace("&amp;","&")
                if (element.data.community_icon.toString().isNotEmpty())
                    subList.add(SubredditItemModel(element.data.display_name_prefixed, element.data.community_icon, element.data.subscribers))
                else
                    subList.add(SubredditItemModel(element.data.display_name_prefixed, element.data.icon_img, element.data.subscribers))
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
                recyclerView.adapter = SubredditListAdapter(this.context, subList, R.layout.home_tab_subreddit_item)
            }
        })
        viewModel.getSubscribedSubreddit()
        return view;
    }
}
