package rocks.mobileera.mobileera.adapters.interfaces

import rocks.mobileera.mobileera.model.Session

interface AddToFavoritesCallback {
    fun onAddToFavoritesClick(session: Session?)
}