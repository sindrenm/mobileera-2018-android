package rocks.mobileera.mobileera.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.interfaces.SpeakerCallback
import rocks.mobileera.mobileera.adapters.viewHolders.SpeakerViewHolder
import rocks.mobileera.mobileera.model.Speaker

class SpeakersAdapter(
        speakersList: List<Speaker>?,
        private val listener: SpeakerCallback?)
    : RecyclerView.Adapter<SpeakerViewHolder>() {

    var speakers = speakersList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val position = v.tag as? Int
            position?.let {
                val speaker = speakers?.getOrNull(position)
                listener?.onSpeakerClick(speaker, R.id.action_navigation_speakers_to_speakerActivity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        return SpeakerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.row_speaker, parent, false))
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        speakers?.getOrNull(position)?.let {item ->
            val previousSpeaker = if (position == 0) null else speakers?.getOrNull(position - 1)
            holder.set(item, previousSpeaker)
        }

        with(holder.view) {
            tag = position
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int {
        val speakers = speakers?.let { it } ?: return 0
        return speakers.size
    }
}
