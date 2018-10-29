package rocks.mobileera.mobileera.utils

import android.content.Context
import android.support.annotation.DrawableRes
import rocks.mobileera.mobileera.R
import rocks.mobileera.mobileera.model.Session

@DrawableRes
fun favoriteIconResForSession(session: Session?, context: Context): Int {
    if (session == null) return R.drawable.star_empty

    return if (session.isFavorite(context)) R.drawable.star_filled else R.drawable.star_empty
}
