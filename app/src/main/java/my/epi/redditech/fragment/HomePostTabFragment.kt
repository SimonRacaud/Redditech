package my.epi.redditech.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.PostListAdapter
import my.epi.redditech.model.PostItemModel

/**
 * Home Posts list tab
 */
class HomePostTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_post_tab_fragment, container, false)

        // TODO : debug data (api call)
        val postList = arrayListOf<PostItemModel>()
        postList.add(PostItemModel("r/programming", "description du post", "https://styles.redditmedia.com/t5_2fwo/styles/communityIcon_1bqa1ibfp8q11.png?width=256&s=45361614cdf4a306d5510b414d18c02603c7dd3c"))
        postList.add(PostItemModel("r/subreddit", "description du post"))
        postList.add(PostItemModel("r/subreddit", "Description du post. Description du sub. Description du sub. Description du sub. Description du sub. Description du sub. Description du sub."))

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = PostListAdapter(this.context, postList, R.layout.home_tab_post_item)

        // Filters selector creation
        val spinner: Spinner = view.findViewById(R.id.filter_selector)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        return view
    }

}
