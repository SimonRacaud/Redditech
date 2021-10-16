package my.epi.redditech.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.SubredditListAdapter
import my.epi.redditech.model.SubredditItemModel

/**
 * Home My Subreddits list tab
 */
class HomeSubredditsTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_subs_tab_fragment, container, false)

        // TODO : debug data (api call)
        val subList = arrayListOf<SubredditItemModel>()
        subList.add(SubredditItemModel("Titre du sub", "https://styles.redditmedia.com/t5_2fwo/styles/communityIcon_1bqa1ibfp8q11.png?width=256&s=45361614cdf4a306d5510b414d18c02603c7dd3c"))
        subList.add(SubredditItemModel("Titre du sub 2", ))
        subList.add(SubredditItemModel("Titre du sub de test 3"))

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = SubredditListAdapter(this.context, subList, R.layout.home_tab_subreddit_item)

        return view;
    }
}
