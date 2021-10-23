package my.epi.redditech.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.epi.redditech.R
import my.epi.redditech.activity.PostPageActivity
import my.epi.redditech.activity.SubredditActivity
import my.epi.redditech.model.PostItemModel
import retrofit2.http.Url

/**
 * Post List Adapter
 */
class PostListAdapter(
    private val context: Context?,
    private val itemList: List<PostItemModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    // Post Item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById<ImageView>(R.id.imageViewPostIcon)
        val subredditName: TextView = view.findViewById<TextView>(R.id.subreddit_name)
        val content: TextView = view.findViewById<TextView>(R.id.content)
        val author: TextView = view.findViewById<TextView>(R.id.author)
        val redirectButton: Button = view.findViewById<Button>(R.id.redirect_button)
        val video: WebView = view.findViewById<WebView>(R.id.video_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        val current = itemList[position]

        if (context != null && !current.thumbnail.isEmpty()) {
//            Glide.with(context).load(Uri.parse(current.thumbnail)).into(holder.icon)
        }
        holder.subredditName.text = current.subredditName
        holder.author.text = current.author

        holder.content.text = current.description
        holder.content.setOnClickListener {
            val intent = Intent(context, PostPageActivity::class.java)
            intent.putExtra("post", current.postUrl)
            context?.startActivity(intent)
        }
        if (current.description.isEmpty()) {
            holder.content.visibility = View.GONE
        }
        ///
        holder.subredditName.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java)
            intent.putExtra("subreddit", current.subredditName)
            context?.startActivity(intent)
        }
        ///
        if (current.redirectUrl != null) {
            val url = Uri.parse(current.redirectUrl)
            if (url.host.isNullOrEmpty() == false) {
                holder.redirectButton.text = "Go to " + url.host
            }
            holder.redirectButton.setOnClickListener {
                val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(current.redirectUrl))
                context?.startActivity(browseIntent)
            }
            holder.redirectButton.visibility = View.VISIBLE
        }
        if (current.video != null && !current.video.media_domain_url.isNullOrEmpty()) {
            holder.video.loadUrl(current.video.media_domain_url)
            holder.video.settings?.allowContentAccess = true
            holder.video.settings?.allowFileAccess = true
            holder.video.settings?.javaScriptEnabled = true
            holder.video.webViewClient = WebViewClient()
            holder.video.visibility = View.VISIBLE
            println("LOAD VIDEO ${current.video.media_domain_url}")
        }
        if (current.preview != null) {
            // TODO load images
        }
    }

    override fun getItemCount(): Int = itemList.size
}
