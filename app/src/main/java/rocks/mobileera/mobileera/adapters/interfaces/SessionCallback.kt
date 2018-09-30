package rocks.mobileera.mobileera.adapters.interfaces

import rocks.mobileera.mobileera.model.Session

interface SessionCallback {
    fun onSessionClick(session: Session?)
}