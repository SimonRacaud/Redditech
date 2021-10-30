package my.epi.redditech.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import my.epi.redditech.R
import my.epi.redditech.activity.SubredditActivity
import my.epi.redditech.model.SubredditItemModel
import android.graphics.Bitmap

import com.bumptech.glide.load.engine.DiskCacheStrategy
import my.epi.redditech.model.PostItemModel
import my.epi.redditech.utils.Utils


/**
 * Subreddit List Adapter
 */
class SubredditListAdapter(
    private val context: Context,
    private val itemList: MutableList<SubredditItemModel>,
    private val layoutId: Int,
) : RecyclerView.Adapter<SubredditListAdapter.ViewHolder>() {

    // Post Item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.imageViewPostIcon)
        val title = view.findViewById<TextView>(R.id.title)
        val subscriberCounter = view.findViewById<TextView>(R.id.subscriber_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = itemList[position]

        holder.title.text = current.title
        holder.itemView.setOnClickListener { onClickListener(current.title) }
        holder.subscriberCounter.text = Utils.getFormatNumber(current.subscribers)
        if (!current.imageUrl.isNullOrEmpty()) {
            val uri = Uri.parse(current.imageUrl)
            Glide.with(context).load(uri).into(holder.icon)
        }
    }

    private fun onClickListener(pageName: String) {
        val intent = Intent(context, SubredditActivity::class.java)
        intent.putExtra("subredditName", pageName)
        context?.startActivity(intent)
    }

    fun append(list: List<SubredditItemModel>) {
        val oldSize = itemList.size
        itemList.addAll(list)
        this.notifyItemRangeInserted(oldSize, list.size)
    }

    fun clear()
    {
        val size = itemList.size

        itemList.clear()
        this.notifyItemRangeRemoved(0, size)
    }

}