package rocks.mobileera.mobileera.adapters.interfaces

import rocks.mobileera.mobileera.model.Speaker

interface SpeakerCallback {
    fun onSpeakerClick(speaker: Speaker?, action: Int)
}