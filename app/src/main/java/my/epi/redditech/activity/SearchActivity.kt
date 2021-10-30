package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.SubredditListAdapter
import my.epi.redditech.model.SubredditItemModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.utils.ErrorMessage
import my.epi.redditech.viewmodel.SearchViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

/**
 * Search subreddit page
 */
class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }

        // SEARCH BAR WIDGET
        val searchBar = findViewById<SearchView>(R.id.searchInput)
        searchBar.isIconified = false
        searchBar.isFocusedByDefault = true
        searchBar.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                loadList(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                loadList(query)
                return false
            }
        })
        /// Load list content (network)
        this.loadList()
    }

    /**
     * Load subreddit list (network)
     * Default : load popular subreddits
     * Search : search of subreddits
     */
    private fun loadList(query: String = "")
    {
        val list = arrayListOf<SubredditItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        viewModel.subredditList.observe(this, {
            it.data.children.forEach { element ->
                element.data.community_icon = element.data.community_icon.toString().replace("&amp;","&")
                if (element.data.community_icon.toString().isNotEmpty())
                    list.add(SubredditItemModel(element.data.display_name_prefixed, element.data.community_icon, element.data.subscribers))
                else
                    list.add(SubredditItemModel(element.data.display_name_prefixed, element.data.icon_img, element.data.subscribers))
            }
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            if (recyclerView.adapter != null) {
                (recyclerView.adapter as SubredditListAdapter).clear()
            }
            recyclerView.adapter = SubredditListAdapter(this, list, R.layout.home_tab_subreddit_item)
        })
        viewModel.errorMessage.observe(this, {
            ErrorMessage.show(this, it)
        })
        if (query.isEmpty()) {
            viewModel.getSubredditList("popular") // Default list items are popular subreddits
        } else {
            viewModel.getSubredditSearch(query)
        }
    }
}