package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.epi.redditech.R
import my.epi.redditech.adapter.PostListAdapter
import my.epi.redditech.databinding.ActivitySubredditBinding
import my.epi.redditech.model.PostItemModel
import my.epi.redditech.model.api.SubredditModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.utils.Utils
import my.epi.redditech.viewmodel.SubredditViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

class SubredditActivity : AppCompatActivity() {

    private lateinit var viewModel: SubredditViewModel
    private lateinit var binding: ActivitySubredditBinding
    private lateinit var subredditInfo : SubredditModel
    private lateinit var subredditNameShort : String
    private lateinit var subredditName : String
    private var subState = false
    private var postFilter = arrayOf("rising", "hot", "new", "top")
    private lateinit var loadingManager: LoadingManager

    private fun changeStatusButtonSub() {
        val subscribeButton = findViewById<Button>(R.id.subscribe_button)
        if (subState) {
            subscribeButton.text = "UNSUBSCRIBE"
        } else {
            subscribeButton.text = "SUBSCRIBE"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit)
        loadingManager = LoadingManager(supportFragmentManager)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_subreddit
        )

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
        /// Get Parameters
        val intent = getIntent()
        subredditName = intent.extras?.get("subredditName").toString()
        subredditNameShort = subredditName.drop(2) // remove the "r/"
        /// Load content
        this.loadMetadata(subredditNameShort)

        // Filters selector creation
        this.createFilterSelector()

    }

    private fun createFilterSelector() {
        val spinner: Spinner = findViewById(R.id.filter_selector)
        ArrayAdapter.createFromResource(
            this,
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (postFilter[position].isNotEmpty()) {
                    loadListContent(subredditName, postFilter[position])
                } else {
                    loadListContent(subredditName, "hot")
                }

            }
        }
    }

    private fun loadListContent(pageName: String, filter: String) {
        val postList = arrayListOf<PostItemModel>()
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(SubredditViewModel::class.java)
        viewModel.subredditPosts.observe(this, {
            postList.clear()
            it.data.children.forEach { element ->
                postList.add(PostItemModel(element.data))
            }
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = PostListAdapter(this, postList, R.layout.home_tab_post_item)
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
        viewModel.getSubredditPosts(pageName, filter)
    }

    private fun loadMetadata(pageName: String) {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)

        loadingManager.startLoading()
        viewModel = ViewModelProvider(this, factory).get(SubredditViewModel::class.java)
        viewModel.subredditInfo.observe(this, {
            binding.lifecycleOwner = this
            binding.subreddit = it.data
            subredditInfo = it.data

            val headerImg = findViewById<ImageView>(R.id.subredit_img_header)
            var bannerUrl = it.data.banner_background_image
            bannerUrl = bannerUrl?.replace("&amp;", "&")
            Glide.with(this).load(bannerUrl).into(headerImg)

            val iconImg = findViewById<ImageView>(R.id.community_icon)
            var comunityIconUrl = it.data.community_icon
            comunityIconUrl = comunityIconUrl?.replace("&amp;", "&")
            Glide.with(this).load(comunityIconUrl).into(iconImg)

            val nbSubscriber = findViewById<TextView>(R.id.header_nb_subscribers)
            nbSubscriber.text = Utils.getFormatNumber(it.data.subscribers)

            val subscribeButton = findViewById<Button>(R.id.subscribe_button)
            if (it.data.user_is_subscriber) {
                subState = true
                subscribeButton.text = "UNSUBSCRIBE"
            } else {
                subState = false
                subscribeButton.text = "SUBSCRIBE"
            }
            subscribeButton.setOnClickListener {
                if (subState) {
                    viewModel.subscribeToSub(subredditNameShort, "unsub")
                } else {
                    viewModel.subscribeToSub(subredditNameShort, "sub")
                }
            }
        })
        viewModel.errorMessage.observe(this, {
            //TODO use it
            loadingManager.stopLoading()
        })
        viewModel.loading.observe(this, {
            if (it) {
                //TODO: SHOW PROGRESS BAR
            } else {
                //TODO: mask progress
                loadingManager.stopLoading()
            }
        })
        viewModel.getInfoSubreddit(pageName)
        viewModel.subscribeAction.observe(this, {
            if (it) {
                subState = !subState
                changeStatusButtonSub()
            }
        })
    }
}