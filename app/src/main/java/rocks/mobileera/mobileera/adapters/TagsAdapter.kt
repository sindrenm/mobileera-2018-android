package rocks.mobileera.mobileera.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.row_tag_clickable.view.*
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.interfaces.TagCallback
import rocks.mobileera.mobileera.model.Tag

class TagsAdapter (
        private val tags: List<String>,
        private val listener: TagCallback?)
    : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val position = v.tag
            if (position is Int) {
                tags.getOrNull(position)?.let {tag ->
                    listener?.onTagClick(tag)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.row_tag_clickable, parent, false))
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tags.getOrNull(position)?.let {tag ->
            holder.tagTextView.text = tag
            holder.tagTextView.setBackgroundResource(Tag.background(tag))
        }

        with(holder.itemView) {
            tag = position
            setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tagTextView: TextView = view.tagTextView
    }
}