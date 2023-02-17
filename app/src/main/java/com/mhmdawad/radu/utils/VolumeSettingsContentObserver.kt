package com.mhmdawad.radu.utils

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.util.Log

class VolumeSettingsContentObserver(private val audioManager: AudioManager, handler: Handler?) :
    ContentObserver(handler) {

    private var onCurrentVolumeChanged: ((Int) -> Unit)? = null

    override fun deliverSelfNotifications(): Boolean {
        return false
    }

    override fun onChange(selfChange: Boolean) {
        val currentVolume: Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        onCurrentVolumeChanged?.let {
            it(currentVolume)
        }
    }

    fun onCurrentVolumeChanged(volumeListener: (Int) -> Unit) {
        onCurrentVolumeChanged = volumeListener
        onCurrentVolumeChanged?.let {
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            it(volume)
        }
    }

    fun destroyVolumeCallback() {
        onCurrentVolumeChanged = null
    }
}