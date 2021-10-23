package my.epi.redditech.adapter

import android.R.attr
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import android.R.attr.data
import org.w3c.dom.Text


/**
 * Subreddit List Adapter
 */
class SubredditListAdapter(
    private val context: Context?,
    private val itemList: MutableList<SubredditItemModel>,
    private val layoutId: Int
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
        if (context != null && current.imageUrl != null && current.imageUrl != "") {
            Glide.with(context).load(Uri.parse(current.imageUrl)).into(holder.icon)
        }
        holder.title.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java) // TODO : Go to subbred page
            intent.putExtra("subredditName", current.title)
            context?.startActivity(intent)
        }
        holder.icon.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java) // TODO : Go to subbred page
            intent.putExtra("subredditName", current.title)
            context?.startActivity(intent)
        }
        holder.subscriberCounter.text = this.getFormatNumber(current.subscribers)
    }

    private fun getFormatNumber(number: Int) : String {
        var str = number.toString()

        if (str.length > 6) {
            str = str.substring(0, str.length - 6) + 'M'
        } else if (str.length > 3) {
            str = str.substring(0, str.length - 3) + 'K'
        }
        return str
    }

    fun clear()
    {
        val size = itemList.size

        itemList.clear()
        this.notifyItemRangeRemoved(0, size)
    }

}