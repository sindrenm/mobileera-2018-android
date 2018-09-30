package rocks.mobileera.mobileera.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.DayAdapter
import rocks.mobileera.mobileera.adapters.interfaces.AddToFavoritesCallback
import rocks.mobileera.mobileera.adapters.interfaces.SessionCallback
import rocks.mobileera.mobileera.adapters.interfaces.TagCallback
import rocks.mobileera.mobileera.model.Day
import rocks.mobileera.mobileera.viewModels.ScheduleViewModel


class DayFragment: Fragment() {

    private val ARGS_DAY_INDEX = "ARGS_DAY_INDEX"
    private val ARGS_TITLE = "ARGS_TITLE"

    private var sessionListener: SessionCallback? = null
    private var tagListener: TagCallback? = null
    private var addToFavoritesListener: AddToFavoritesCallback? = null

    private var title: CharSequence? = ""
    private var dayIndex: Int = 0

    private val isWorkshopsDay: Boolean
        get() = dayIndex == 0

    private var dayAdapter: DayAdapter? = null

    var day: Day? = null
        set(value) {
            field = value
            print("Data has been updated")
            dayAdapter?.day = value
        }


    companion object {
        internal fun newInstance(title: CharSequence?, dayIndex: Int): DayFragment {
            val fragment = DayFragment()
            fragment.title = title
            fragment.dayIndex = dayIndex
            return fragment
        }
    }

    override fun toString(): String {
        title?.let {title ->
            if (title is String) {
                return title
            }
        }

        return ""
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddToFavoritesCallback) {
            addToFavoritesListener = context
        }

        if (context is TagCallback) {
            tagListener = context
        }

        if (context is SessionCallback) {
            sessionListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        addToFavoritesListener = null
        tagListener = null
        sessionListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        parentFragment?.let {
            dayAdapter = DayAdapter(activity?.applicationContext, day, sessionListener, addToFavoritesListener, tagListener)

            val viewModel = ViewModelProviders.of(it).get(ScheduleViewModel::class.java)
            viewModel.getDays()?.observe(this, Observer<List<Day>> { days ->
                day = days?.getOrNull(dayIndex)
            })

            viewModel.selectedTags.observe(this, Observer<List<String>> { _ ->
                dayAdapter?.doFilter()
                dayAdapter?.notifyDataSetChanged()
            })

            viewModel.showOnlyFavorite.observe(this, Observer<Boolean> { _ ->
                dayAdapter?.doFilter()
                dayAdapter?.notifyDataSetChanged()
            })
        }

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = dayAdapter
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getInt(ARGS_DAY_INDEX, 0)?.let { dayIndex = it }
        savedInstanceState?.getString(ARGS_TITLE, "")?.let { title = it }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARGS_DAY_INDEX, dayIndex)
        title?.let { outState.putString(ARGS_TITLE, it.toString()) }
    }
}