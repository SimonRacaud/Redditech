package my.epi.redditech.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import my.epi.redditech.utils.ErrorMessage
import my.epi.redditech.utils.LoadingManager
import my.epi.redditech.utils.Utils
import my.epi.redditech.viewmodel.SubredditViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

/**
 * Subreddit home page
 */
class SubredditActivity : AppCompatActivity() {

    private lateinit var viewModel: SubredditViewModel
    private lateinit var binding: ActivitySubredditBinding
    private lateinit var subredditInfo: SubredditModel
    private lateinit var subredditNameShort: String
    private lateinit var subredditName: String
    private var subState = false
    private var postFilter = arrayOf("hot", "new", "top", "rising")
    private lateinit var loadingManager: LoadingManager
    private var filterPosition: Int = 0
    private var nextPost: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit)
        loadingManager = LoadingManager(supportFragmentManager)

        /// Bind data with the view
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_subreddit
        )

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
        // Filters selector creation
        this.createFilterSelector()
        /// Get Parameters
        val intent = getIntent()
        subredditName = intent.extras?.get("subredditName").toString()
        subredditNameShort = subredditName.drop(2) // remove the "r/"
        /// Load content
        this.loadMetadata(subredditNameShort)
        /// Infinite scroll
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter =
            PostListAdapter(this, arrayListOf<PostItemModel>(), R.layout.home_tab_post_item)
        this.handleInfiniteScroll(recyclerView)
    }

    /**
     * For pagination : detect scrolling end
     */
    private fun handleInfiniteScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadListContent(subredditName, postFilter[filterPosition], false)
                    Log.d("SUBREDDIT ACTIVITY", "on ne peut plus scroll")
                }
            }
        })
    }

    /**
     * Subscription button - update status
     */
    private fun changeStatusButtonSub() {
        val subscribeButton = findViewById<Button>(R.id.subscribe_button)
        if (subState) {
            subscribeButton.text = "UNSUBSCRIBE"
        } else {
            subscribeButton.text = "SUBSCRIBE"
        }
    }

    /**
     * Init filter selector
     */
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
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterPosition = position
                if (postFilter[filterPosition].isNotEmpty()) {
                    nextPost = ""
                    loadListContent(subredditName, postFilter[filterPosition], true)
                } else {
                    nextPost = ""
                    loadListContent(subredditName, "hot", true)
                }
            }
        }
    }

    /**
     * Load subreddit's post list
     */
    private fun loadListContent(pageName: String, filter: String, reset: Boolean) {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val postList = arrayListOf<PostItemModel>()

        viewModel = ViewModelProvider(this, factory).get(SubredditViewModel::class.java)
        if (reset) {
            (recyclerView.adapter as PostListAdapter).clear()
        }
        viewModel.subredditPosts.observe(this, {
            postList.clear()
            if (nextPost != it.data.after.toString() && nextPost != null) {
                nextPost = it.data.after.toString()
                it.data.children.forEach { element ->
                    postList.add(PostItemModel(element.data))
                }
                (recyclerView.adapter as PostListAdapter).append(postList)
            }
        })
        viewModel.errorMessage.observe(this, {
            ErrorMessage.show(this, it)
        })
        viewModel.getSubredditPosts(pageName, filter, nextPost!!)
    }

    /**
     * Load subreddit meta data
     */
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
            if (bannerUrl.isNullOrEmpty()) {
                var background = findViewById<TextView>(R.id.background)
                background.visibility = View.VISIBLE
                headerImg.visibility = View.INVISIBLE
                if (!it.data.key_color.isNullOrEmpty()) {
                    background.background = ColorDrawable(Color.parseColor(it.data.key_color))
                } else if (!it.data.banner_background_color.isNullOrEmpty()) {
                    background.background = ColorDrawable(Color.parseColor(it.data.banner_background_color))
                } else if (!it.data.primary_color.isNullOrEmpty()) {
                    background.background = ColorDrawable(Color.parseColor(it.data.primary_color))
                }

            } else {
                bannerUrl = bannerUrl?.replace("&amp;", "&")
                Glide.with(this).load(bannerUrl).into(headerImg)
            }


            val iconImg = findViewById<ImageView>(R.id.community_icon)
            var comunityIconUrl = ""
            if (subredditInfo.community_icon.toString().isNotEmpty()) {
                comunityIconUrl = subredditInfo.community_icon.toString()
            } else {
                comunityIconUrl = subredditInfo.icon_img
            }
            comunityIconUrl = comunityIconUrl?.replace("&amp;", "&")
            Glide.with(this).load(comunityIconUrl).into(iconImg)

            val nbSubscriber = findViewById<TextView>(R.id.header_nb_subscribers)
            nbSubscriber.text = Utils.getFormatNumber(it.data.subscribers)

            val nbConnected = findViewById<TextView>(R.id.header_nb_connected)
            nbConnected.text = Utils.getFormatNumber(it.data.active_user_count)

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
            ErrorMessage.show(this, it)
            loadingManager.stopLoading()
        })
        viewModel.loading.observe(this, {
            if (!it) {
                loadingManager.stopLoading() // end loading
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