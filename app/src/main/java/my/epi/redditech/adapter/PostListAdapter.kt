package my.epi.redditech.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.epi.redditech.MediaWebViewClient
import my.epi.redditech.R
import my.epi.redditech.activity.PostPageActivity
import my.epi.redditech.activity.SubredditActivity
import my.epi.redditech.model.PostItemModel

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
        val media: WebView = view.findViewById<WebView>(R.id.media_view)
        val picture: ImageView = view.findViewById<ImageView>(R.id.picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        val current = itemList[position]

        if (context != null && !current.thumbnail.isEmpty()) {
            Glide.with(context).load(Uri.parse(current.thumbnail)).into(holder.icon)
        }
        holder.subredditName.text = current.subredditName
        holder.author.text = current.author

        if (current.title.isEmpty() == false) {
            holder.content.text = current.title
            holder.content.setOnClickListener {
                val intent = Intent(context, PostPageActivity::class.java)
                intent.putExtra("post", current.postUrl)
                context?.startActivity(intent)
            }
            holder.content.visibility = View.VISIBLE
        }
        ///
        holder.subredditName.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java)
            intent.putExtra("subreddit", current.subredditName)
            context?.startActivity(intent)
        }
        ///
        if (current.media != null && !current.media.media_domain_url.isNullOrEmpty()) {
            holder.media.loadUrl(current.media.media_domain_url)
            holder.media.settings?.allowContentAccess = true
            holder.media.settings?.allowFileAccess = true
            holder.media.settings?.javaScriptEnabled = true
            holder.media.webViewClient = MediaWebViewClient()
            holder.media.visibility = View.VISIBLE
        } else if (current.redirectUrl != null) {
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
        // show image
        if (context != null && !current.redirectUrl.isNullOrEmpty()
            && !(current.media != null && !current.media.media_domain_url.isNullOrEmpty())) {
            val extension = current.redirectUrl.substringAfterLast('.')
            if (extension == "png" || extension == "jpg") {
                Glide.with(context).load(Uri.parse(current.redirectUrl)).into(holder.picture)
                holder.picture.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = itemList.size
}
