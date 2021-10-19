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
import my.epi.redditech.R
import my.epi.redditech.activity.SubredditActivity
import my.epi.redditech.model.SubredditItemModel

/**
 * Subreddit List Adapter
 */
class SubredditListAdapter(
    private val context: Context?,
    private val itemList: List<SubredditItemModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<SubredditListAdapter.ViewHolder>() {

    // Post Item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.imageViewPostIcon)
        val title = view.findViewById<TextView>(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = itemList[position]

        holder.title.text = current.title
        if (context != null && current.imageUrl != null) {
            Glide.with(context).load(Uri.parse(current.imageUrl)).into(holder.icon)
        }
        holder.title.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java) // TODO : Go to subbred page
            context?.startActivity(intent)
        }
        holder.icon.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java) // TODO : Go to subbred page
            context?.startActivity(intent)
        }
    }
}