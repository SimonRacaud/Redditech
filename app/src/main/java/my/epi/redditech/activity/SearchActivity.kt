package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.SubredditListAdapter
import my.epi.redditech.model.SubredditItemModel


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }

        // SEARCH BAR
        val searchBar = findViewById<SearchView>(R.id.searchInput)
        searchBar.isIconified = false
        searchBar.isFocusedByDefault = true

        // SUBREDDIT LIST
        // TODO : debug data (api call)
        val subList = arrayListOf<SubredditItemModel>()
        subList.add(SubredditItemModel("Titre du sub", "https://styles.redditmedia.com/t5_2fwo/styles/communityIcon_1bqa1ibfp8q11.png?width=256&s=45361614cdf4a306d5510b414d18c02603c7dd3c"))
        subList.add(SubredditItemModel("Titre du sub 2", ))
        subList.add(SubredditItemModel("Titre du sub de test 3"))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = SubredditListAdapter(this, subList, R.layout.home_tab_subreddit_item)

    }
}