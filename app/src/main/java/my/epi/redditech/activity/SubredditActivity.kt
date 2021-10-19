package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import my.epi.redditech.R
import my.epi.redditech.adapter.PostListAdapter
import my.epi.redditech.model.PostItemModel

class SubredditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit)

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }

        // TODO : debug data (api call)
        val postList = arrayListOf<PostItemModel>()
        postList.add(PostItemModel("Titre du post", "description du post", "https://styles.redditmedia.com/t5_2fwo/styles/communityIcon_1bqa1ibfp8q11.png?width=256&s=45361614cdf4a306d5510b414d18c02603c7dd3c"))
        postList.add(PostItemModel("Titre du post 2", "description du post"))
        postList.add(PostItemModel("Titre du post de test 3", "Description du post. Description du sub. Description du sub. Description du sub. Description du sub. Description du sub. Description du sub."))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = PostListAdapter(this.applicationContext, postList, R.layout.home_tab_post_item)

        // Filters selector creation
        val spinner: Spinner = findViewById(R.id.filter_selector)
        ArrayAdapter.createFromResource(
            this,
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}