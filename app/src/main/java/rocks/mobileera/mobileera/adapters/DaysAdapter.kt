package rocks.mobileera.mobileera.adapters
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.fragments.DayFragment

class DaysAdapter(fragmentManager: FragmentManager?, private val context: Context?) : FragmentPagerAdapter(fragmentManager) {
    override fun getPageTitle(position: Int): CharSequence? {
        context?.let {
            when (position) {
                0 -> return context.getString(R.string.workshops)
                1 -> return context.getString(R.string.day1)
                2 -> return context.getString(R.string.day2)
            }

            return null
        }

        return null
    }

    override fun getItem(position: Int): Fragment {
        return DayFragment.newInstance(getPageTitle(position), position)
    }

    override fun getCount(): Int {
        return 3
    }

}