package rocks.mobileera.mobileera.adapters.viewHolders

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_speaker.view.*
import rocks.mobileera.mobileera.model.Speaker
import rocks.mobileera.mobileera.utils.CircleTransform
import rocks.mobileera.mobileera.utils.Preferences

class SpeakerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView: TextView = view.nameTextView
    val companyTextView: TextView = view.companyTextView
    val avatarImageView: ImageView = view.avatarImageView
    val twitterImageView: ImageView = view.twitterImageView
    val githubImageView: ImageView = view.githubImageView
    val webImageView: ImageView = view.webImageView
    val indexTextView: TextView = view.indexTextView

    private var speaker: Speaker? = null

    fun set(speaker: Speaker?) {
        set(speaker, null)
        indexTextView.visibility = View.GONE
    }

    fun set(speaker: Speaker?, previousSpeaker: Speaker?) {
        this.speaker = speaker

        speaker?.let { it
            nameTextView.text = speaker.name
            companyTextView.text = speaker.company

            indexTextView.visibility = if (speaker.name.firstOrNull() == previousSpeaker?.name?.firstOrNull()) View.INVISIBLE else View.VISIBLE
            indexTextView.text = speaker.name.firstOrNull()?.toString()

            Picasso.get().load(Uri.parse(Preferences.domain + speaker.photoUrl)).transform(CircleTransform()).into(avatarImageView)

            setSocials(speaker)
        }
    }

    private fun setSocials(speaker: Speaker) {
        speaker.socials?.let { socials ->
            twitterImageView.visibility = if (socials.any { social -> social.icon == "twitter"}) View.VISIBLE else View.GONE
            githubImageView.visibility = if (socials.any { social -> social.icon == "github"}) View.VISIBLE else View.GONE
            webImageView.visibility = if (socials.any { social -> social.icon == "website"}) View.VISIBLE else View.GONE
        } ?: run {
            twitterImageView.visibility = View.GONE
            githubImageView.visibility = View.GONE
            webImageView.visibility = View.GONE
        }
    }
}
