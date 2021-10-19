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
import my.epi.redditech.activity.PostPageActivity
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
        val icon = view.findViewById<ImageView>(R.id.imageViewPostIcon)
        val title = view.findViewById<TextView>(R.id.title)
        val content = view.findViewById<TextView>(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        val current = itemList[position]

        if (context != null && current.imageUrl != null) {
            Glide.with(context).load(Uri.parse(current.imageUrl)).into(holder.icon)
        }
        holder.title.text = current.title
        holder.content.text = current.description

        holder.title.setOnClickListener {
            val intent = Intent(context, PostPageActivity::class.java) // TODO : Go to post page
            context?.startActivity(intent)
        }
        holder.icon.setOnClickListener {
            val intent = Intent(context, PostPageActivity::class.java) // TODO : Go to post page
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
