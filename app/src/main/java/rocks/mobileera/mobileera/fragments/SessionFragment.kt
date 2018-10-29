package rocks.mobileera.mobileera.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_session.*

import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.viewHolders.SpeakerViewHolder
import rocks.mobileera.mobileera.model.Session
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.CalendarContract.Events
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import rocks.mobileera.mobileera.adapters.TagsAdapter
import rocks.mobileera.mobileera.adapters.interfaces.SpeakerCallback
import rocks.mobileera.mobileera.utils.favoriteIconResForSession
import java.text.SimpleDateFormat
import java.util.*


class SessionFragment : Fragment() {

    private val REQUEST_CODE_CALENDAR = 1
    private var speakerListener: SpeakerCallback? = null

    companion object {

        private val ARGS_SESSION_JSON = "ARGS_SESSION_JSON"

        fun createBundle(session: Session): Bundle {
            val bundle = Bundle()
            bundle.putString(ARGS_SESSION_JSON, Gson().toJson(session))
            return bundle
        }
    }


    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val position = v.tag as? Int
            position?.let {
                val speaker = session?.speakersList?.getOrNull(position)
                speakerListener?.onSpeakerClick(speaker, R.id.action_sessionFragment_to_speakerActivity)
            }
        }
    }

    private val dateFormatter = SimpleDateFormat("E, MMMM d / HH:mm", Locale.US)
    private var session: Session? = null
    private fun updateState(arguments: Bundle?) {
        arguments?.let { value ->
            session = Gson().fromJson(value.getString(ARGS_SESSION_JSON), Session::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        updateState(arguments)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_session, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        activity?.applicationContext?.let {
            menu?.findItem(R.id.btnAddToFavorites)?.setIcon(favoriteIconResForSession(session, it))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.btnAddToFavorites -> {
                activity?.applicationContext?.let {
                    session?.toggleFavorites(it)
                    item.setIcon(favoriteIconResForSession(session, it))
                }
            }

            R.id.btnAddToCalendar -> {
                activity?.let {
                    if (ContextCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(android.Manifest.permission.WRITE_CALENDAR), REQUEST_CODE_CALENDAR)
                    } else {
                        addSessionToCalendar()
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addSessionToCalendar() {
        val intent = Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, session?.startDate?.time)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, session?.endDate?.time)
                .putExtra(Events.TITLE, session?.title)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            print(e)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CALENDAR -> addSessionToCalendar()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        titleTextView.text = (session?.title + (if (session?.lightning == true) " ⚡" else ""))

        subtitleTextView.text = session?.language
        session?.startDate?.let {date ->
            subtitleTextView.text = dateFormatter.format(date)
            session?.language?.let { language ->
                subtitleTextView.text = (dateFormatter.format(date) + if (language.isEmpty()) "" else (" / $language"))
            }
        }

        session?.tags?.let { tags ->
            tagsRecyclerView.visibility = View.VISIBLE
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW_REVERSE
            layoutManager.justifyContent = JustifyContent.FLEX_END
            tagsRecyclerView.layoutManager = layoutManager
            tagsRecyclerView.adapter = TagsAdapter(tags, null)

            activity?.applicationContext?.resources?.getDrawable(R.drawable.divirer_tags_horizontal)?.let {
                val dividerItemDecoration = DividerItemDecoration(context,
                        RecyclerView.VERTICAL)
                dividerItemDecoration.setDrawable(it)
                tagsRecyclerView.addItemDecoration(dividerItemDecoration)
            }

        } ?: run {
            tagsRecyclerView.visibility = View.GONE
        }

        sessionTextView.text = session?.description

        session?.speakersList?.getOrNull(0)?.let {
            SpeakerViewHolder(speaker1Layout).set(it)
            speaker1Layout.visibility = View.VISIBLE
        } ?: run {
            speaker1Layout.visibility = View.GONE
        }

        session?.speakersList?.getOrNull(1)?.let {
            SpeakerViewHolder(speaker2Layout).set(it)
            speaker2Layout.visibility = View.VISIBLE
        } ?: run {
            speaker2Layout.visibility = View.GONE
        }

        speaker1Layout.tag = 0
        speaker2Layout.tag = 0
        speaker1Layout.setOnClickListener(onClickListener)
        speaker2Layout.setOnClickListener(onClickListener)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SpeakerCallback) {
            speakerListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        speakerListener = null
    }
}
