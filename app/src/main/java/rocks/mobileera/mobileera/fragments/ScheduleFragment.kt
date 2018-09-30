package rocks.mobileera.mobileera.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_schedule.*

import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.adapters.DaysAdapter
import rocks.mobileera.mobileera.model.Day
import rocks.mobileera.mobileera.viewModels.ScheduleViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import rocks.mobileera.mobileera.utils.Preferences


class ScheduleFragment : BaseFragment() {

    lateinit var viewModel: ScheduleViewModel
    private lateinit var adapter: DaysAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DaysAdapter(childFragmentManager, activity?.applicationContext)
        daysViewPager.adapter = adapter
        daysViewPager.currentItem = 1
        daysViewPager.offscreenPageLimit = 2
        daysTabLayout.setupWithViewPager(daysViewPager)

        progressCircular.visibility = View.VISIBLE

        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        viewModel.getDays()?.observe(this, Observer<List<Day>> {days ->
            progressCircular.visibility = View.GONE
        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.navigation_filter -> {
                val tags = ArrayList(viewModel.allTags)
                activity?.let {context ->

                    if (!tags.isEmpty()) {

                        tags.add(0, context.getString(R.string.filter_show_only_favs))

                        val dialog = MaterialDialog.Builder(context)
                                .title(R.string.filter)
                                .items(tags)
                                .alwaysCallMultiChoiceCallback()
                                .itemsCallbackMultiChoice(null) { dialog, selectedIndexes, text ->

                                    Preferences(context.applicationContext).selectedTags = ArrayList()

                                    val selectedTags = ArrayList<String>()
                                    var showOnlyFavs = false
                                    for (index in selectedIndexes) {
                                        if (index == 0) {
                                            showOnlyFavs = true
                                            continue
                                        }

                                        tags.getOrNull(index)?.let { tag ->
                                            selectedTags.add(tag)
                                        }
                                    }

                                    Preferences(context.applicationContext).showOnlyFavorite = showOnlyFavs
                                    Preferences(context.applicationContext).selectedTags = selectedTags

                                    viewModel.showOnlyFavorite.value = showOnlyFavs
                                    viewModel.selectedTags.value = selectedTags

                                    item.setIcon(if (!selectedTags.isEmpty() || showOnlyFavs) R.drawable.ic_filter_active else R.drawable.ic_filter_normal)

                                    true

                                }
                                .onPositive { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .onNeutral { dialog, _ ->
                                    dialog.clearSelectedIndices()
                                }
                                .positiveText(R.string.filter_ok)
                                .neutralText(R.string.filter_clear)
                                .autoDismiss(false)
                                .positiveColorRes(R.color.colorPrimary)
                                .theme(Theme.LIGHT)
                                .show()

                        val selectedIndices: ArrayList<Int> = ArrayList()
                        if (Preferences(context).showOnlyFavorite) {
                            selectedIndices.add(0)
                        }

                        for (selectedTag in Preferences(context.applicationContext).selectedTags) {
                            selectedIndices.add(tags.indexOf(selectedTag) + 1) // +1 - "Show only favorites" option, it's not part of selectedTags
                        }

                        dialog.setSelectedIndices(selectedIndices.toTypedArray())
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_schedule, menu)
        activity?.applicationContext?.let {

            menu?.findItem(R.id.navigation_filter)?.setIcon(
                    if (!Preferences(it).selectedTags.isEmpty() || Preferences(it).showOnlyFavorite) R.drawable.ic_filter_active else R.drawable.ic_filter_normal)
        }
    }
}
