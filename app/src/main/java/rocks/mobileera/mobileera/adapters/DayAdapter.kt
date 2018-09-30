package rocks.mobileera.mobileera.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.row_header_timeslot.view.*
import kotlinx.android.synthetic.main.row_session.view.*
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.interfaces.AddToFavoritesCallback
import rocks.mobileera.mobileera.adapters.interfaces.SessionCallback
import rocks.mobileera.mobileera.adapters.interfaces.TagCallback
import rocks.mobileera.mobileera.adapters.viewHolders.SessionViewHolder
import rocks.mobileera.mobileera.model.Day
import rocks.mobileera.mobileera.model.Legend

import rocks.mobileera.mobileera.utils.Preferences
import rocks.mobileera.mobileera.model.Session
import rocks.mobileera.mobileera.model.Timeslot

class DayAdapter(
        private val context: Context?,
        day: Day?,
        private val sessionListener: SessionCallback?,
        private val addToFavoritesListener: AddToFavoritesCallback?,
        private val tagsListener: TagCallback?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onSessionClickListener: View.OnClickListener
    private val VIEW_HOLDER_TYPE_HEADER = 0
    private val VIEW_HOLDER_TYPE_SESSION = 1
    private val VIEW_HOLDER_TYPE_LEGEND = 2
    private var data: ArrayList<Any> = ArrayList()

    var day = day
        set(value) {
            field = value
            doFilter()
            notifyDataSetChanged()
        }

    init {
        onSessionClickListener = View.OnClickListener { v ->
            val position = v.tag as? Int
            position?.let {
                val session = data.getOrNull(position)
                if (session is Session) {
                   sessionListener?.onSessionClick(session)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {

        if (viewType == VIEW_HOLDER_TYPE_HEADER) {
            return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_header_timeslot, parent, false))
        }

        if (viewType == VIEW_HOLDER_TYPE_SESSION) {
            return SessionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_session, parent, false), tagsListener, addToFavoritesListener)
        }

        return LegendViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_legend, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        data.getOrNull(position)?.let {item ->
            if (item is Timeslot) {
                return VIEW_HOLDER_TYPE_HEADER
            }

            if (item is Session) {
                return VIEW_HOLDER_TYPE_SESSION
            }

            if (item is Legend) {
                return VIEW_HOLDER_TYPE_LEGEND
            }
        }

        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data.getOrNull(position)?.let {item ->
            if (item is Timeslot && holder is HeaderViewHolder) {
                var text = ""
                if (!item.startTime.isEmpty() && !item.endTime.isEmpty()) {
                    text = item.startTime + " - " + item.endTime
                }

                holder.timeTextView.text = text
            }

            if (item is Session && holder is SessionViewHolder) {
                holder.set(context, item, getTrack(position))
                with(holder.view) {
                    tag = position
                    setOnClickListener(onSessionClickListener)
                }
            }
        }

        with(holder.itemView) {
            tag = position
            setOnClickListener(onSessionClickListener)
        }
    }

    /*
        There's no easy way to determine in which track session happens.
        However, json contains [Odin, Freyja, Thon, Odin, Freyja, Thon,...] pattern - so we're counting position away from section-header
     */
    fun getTrack(position: Int): Int {
        if (data.getOrNull(position-1) is Timeslot) {
            return 0
        }

        if (data.getOrNull(position-2) is Timeslot) {
            return 1
        }

        if (data.getOrNull(position-3) is Timeslot) {
            return 2
        }

        return 0
    }

    fun doFilter() {
        data.clear()

        val context = context?.let { it } ?: return
        val timeslots = day?.timeslots?.let { it } ?: return
        var hasSessions: Boolean = false

        for (timeslot in timeslots) {
            data.add(timeslot)

            timeslot.sessionsList?.let {    sessions ->
                for (session in sessions) {
                    if (isMatchingFilter(context, session)) {
                        data.add(session)

                        if (!session.isWorkshop()) {
                            hasSessions = true
                        }
                    }
                }
            }
        }

        if (hasSessions) {
            data.add(Legend())
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun isMatchingFilter(context: Context, session: Session): Boolean {
        val showOnlyFavorite = Preferences(context).showOnlyFavorite
        if (showOnlyFavorite && !session.isFavorite(context)) {
            return false
        }

        val selectedTags = Preferences(context).selectedTags
        if (!selectedTags.isEmpty()) {

            return session.tags?.any { tag ->
                return selectedTags.contains(tag)
            }?.let { it } ?: return false
        }

        return true
    }

    inner class LegendViewHolder(val view: View) : RecyclerView.ViewHolder(view) {}

    inner class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.timeTextView
    }
}
