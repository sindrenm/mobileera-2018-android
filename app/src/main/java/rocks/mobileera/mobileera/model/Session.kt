package rocks.mobileera.mobileera.model

import android.content.Context
import android.content.SharedPreferences
import rocks.mobileera.mobileera.utils.Preferences
import java.util.*

class Session {

    val KEY_FAVORITE_SESSION: String = "KEY_FAVORITE_SESSION_"

    var id: Int = 0
    var title: String = ""
    var description: String = ""
    var image: String? = null
    var language: String? = null
    var lightning: Boolean? = null
    var speakers: List<String> = ArrayList()
    var speakersList: List<Speaker> = ArrayList()
    var tags: List<String> = ArrayList()
    var price: String? = null

    var startDate: Date? = null
    var endDate: Date? = null

    fun duration(): Long {
        val startDate = startDate?.let { it } ?: return 0
        val endDate = endDate?.let { it } ?: return 0

        return endDate.time - startDate.time
    }

    fun isWorkshop(): Boolean {
        return id >= 200 && id < 300
    }

    fun isSystemAnnounce(): Boolean {
        return id < 99
    }

    fun isFavorite(context: Context): Boolean {
        if (isSystemAnnounce()) {
            return false // lunch, system announce, etc.
        }

        return Preferences(context).read(KEY_FAVORITE_SESSION + id.toString())
    }

    fun toggleFavorites(context: Context) {
        if (isSystemAnnounce()) {
            return
        }

        val oldValue = isFavorite(context)
        Preferences(context).store(KEY_FAVORITE_SESSION + id.toString(), !oldValue)
    }
}
