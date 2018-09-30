package rocks.mobileera.mobileera.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_speakers.*
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.SpeakersAdapter
import rocks.mobileera.mobileera.adapters.interfaces.SpeakerCallback
import rocks.mobileera.mobileera.model.Speaker
import rocks.mobileera.mobileera.viewModels.SpeakersViewModel

class SpeakersFragment : BaseFragment() {

    private var listener: SpeakerCallback? = null
    private lateinit var viewModel: SpeakersViewModel
    private lateinit var speakersAdapter: SpeakersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_speakers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        speakersAdapter = SpeakersAdapter(null, listener)
        speakersRecyclerView.layoutManager = LinearLayoutManager(context)
        speakersRecyclerView.adapter = speakersAdapter
        viewModel = ViewModelProviders.of(this).get(SpeakersViewModel::class.java)
        viewModel.getSpeakers()?.observe(this, Observer<List<Speaker>> { speakers ->
            speakersAdapter.speakers = speakers
            progressCircular.visibility = View.GONE
        })

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SpeakerCallback) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_speakers, menu)
    }
}
