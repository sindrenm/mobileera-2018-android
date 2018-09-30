package rocks.mobileera.mobileera.utils

import android.content.Context

class Preferences(private var context: Context) {

    companion object {
        val domain = "https://2017.mobileera.rocks"
    }

    private val SHARED_PREFERENCES_KEY: String = "SHARED_PREFERENCES_KEY"

    private val SELECTED_TAGS: String = "SELECTED_TAGS"
    private val SHOW_ONLY_FAVORITES: String = "SHOW_ONLY_FAVORITES"

    fun store(key: String, value: Boolean) {
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).
                edit().
                putBoolean(key, value).
                apply()
    }

    fun store(key: String, value: List<String>) {
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).
                edit().
                putString(key, value.joinToString(separator = "•")).
                apply()
    }

    fun read(key: String): Boolean {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).getBoolean(key, false)
    }

    private fun readArray(key: String): ArrayList<String> {
        val value = context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).getString(key, "")
        if (value.isEmpty()) {
            return ArrayList()
        }

        return ArrayList(value.split("•"))
    }

    var selectedTags: ArrayList<String>
        get() = readArray(SELECTED_TAGS)
        set(value) {
            store(SELECTED_TAGS, value)
        }


    var showOnlyFavorite: Boolean
        get() = read(SHOW_ONLY_FAVORITES)
        set(value) {
            store(SHOW_ONLY_FAVORITES, value)
        }

}