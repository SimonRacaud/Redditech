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
import my.epi.redditech.viewmodel.SearchViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory


class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel

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
        /// Load list content
        this.loadList()
    }

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
            //TODO use it
        })
        viewModel.loading.observe(this, {
            if (it) {
                //TODO: SHOW PROGRESS BAR
            } else {
                //TODO: mask progress
            }
        })
        if (query.isEmpty()) {
            viewModel.getSubredditList("popular")
        } else {
            viewModel.getSubredditSearch(query)
        }
    }
}