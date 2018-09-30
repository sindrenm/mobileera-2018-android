package rocks.mobileera.mobileera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_speaker.*
import rocks.mobileera.mobileera.model.Speaker
import rocks.mobileera.mobileera.utils.Preferences

class SpeakerActivity : AppCompatActivity() {

    private var speaker: Speaker? = null

    companion object {

        private val ARGS_SPEAKER_JSON = "ARGS_SPEAKER_JSON"

        fun createBundle(speaker: Speaker): Bundle {
            val bundle = Bundle()
            bundle.putString(ARGS_SPEAKER_JSON, Gson().toJson(speaker))
            return bundle
        }
    }

    private fun updateState(arguments: Bundle?) {
        arguments?.let { value ->
            speaker = Gson().fromJson(value.getString(ARGS_SPEAKER_JSON), Speaker::class.java)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaker)
        updateState(intent.extras)
        setSupportActionBar(collapsingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = speaker?.name

        speaker?.let {
            Picasso.get().load(Uri.parse(Preferences.domain + it.photoUrl)).into(avatarImageView)
        } ?: run {
            avatarImageView.setImageResource(android.R.color.transparent)
        }

        companyTextView.text = speaker?.company
        bioTextView.text = speaker?.bio

        speaker?.socials?.let { socials ->
            twitterImageView.visibility = if (socials.any { social -> social.icon == "twitter"}) View.VISIBLE else View.GONE
            githubImageView.visibility = if (socials.any { social -> social.icon == "github"}) View.VISIBLE else View.GONE
            webImageView.visibility = if (socials.any { social -> social.icon == "website"}) View.VISIBLE else View.GONE
        } ?: run {
            twitterImageView.visibility = View.GONE
            githubImageView.visibility = View.GONE
            webImageView.visibility = View.GONE
        }

        twitterImageView?.setOnClickListener { v ->
            speaker?.socials?.first { it.icon == "twitter" }?.link?.let {
                openUrl(it)
            }
        }

        githubImageView?.setOnClickListener { v ->
            speaker?.socials?.first { it.icon == "github" }?.link?.let {
                openUrl(it)
            }
        }

        webImageView?.setOnClickListener { v ->
            speaker?.socials?.first { it.icon == "website" }?.link?.let {
                openUrl(it)
            }
        }

        socialLinearLayout.visibility = if (twitterImageView.visibility == View.VISIBLE ||
                githubImageView.visibility == View.VISIBLE ||
                webImageView.visibility == View.VISIBLE) View.VISIBLE else View.GONE
    }

    private fun openUrl(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        try {
            startActivity(browserIntent)
        } catch (e: Exception) {
            print(e)
        }
    }
}
