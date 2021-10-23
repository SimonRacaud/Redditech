package my.epi.redditech.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.epi.redditech.R
import my.epi.redditech.adapter.PostListAdapter
import my.epi.redditech.databinding.ActivitySubredditBinding
import my.epi.redditech.model.PostItemModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.viewmodel.SubredditViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

class SubredditActivity : AppCompatActivity() {

    private lateinit var viewModel: SubredditViewModel
    private lateinit var binding: ActivitySubredditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_subreddit
        )

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }

        val intent = getIntent()
        var subredditName = intent.extras?.get("subredditName").toString()
        subredditName = subredditName.drop(2)

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SubredditViewModel::class.java)
        viewModel.subredditInfo.observe(this, {
            binding.lifecycleOwner = this
            binding.subreddit = it.data

            val headerImg = findViewById<ImageView>(R.id.subredit_img_header)
            var bannerUrl = it.data.banner_background_image
            bannerUrl = bannerUrl?.replace("&amp;", "&")
            Glide.with(this).load(bannerUrl).into(headerImg)

            val iconImg = findViewById<ImageView>(R.id.community_icon)
            var comunityIconUrl = it.data.community_icon
            comunityIconUrl = comunityIconUrl?.replace("&amp;", "&")
            Glide.with(this).load(comunityIconUrl).into(iconImg)

            val nbSubscriber = findViewById<TextView>(R.id.header_nb_subscribers)
            nbSubscriber.setText(it.data.subscribers.toString())

            val subscribeButton = findViewById<Button>(R.id.subscribe_button)
            if (it.data.user_is_subscriber) {
                subscribeButton.setText("UNSUBSCRIBE")
            } else {
                subscribeButton.setText("SUBSCRIBE")
            }

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
        viewModel.getInfoSubreddit(subredditName)

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