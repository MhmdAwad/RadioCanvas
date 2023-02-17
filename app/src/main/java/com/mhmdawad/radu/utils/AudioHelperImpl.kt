package com.mhmdawad.radu.utils

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AudioHelperImpl @Inject constructor(@ApplicationContext val context: Context) : AudioHelper {
    private val volumeSettingsContentObserver by lazy {
        VolumeSettingsContentObserver(audioManager, Handler(Looper.getMainLooper()))
    }
    private val audioManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    init {
        context.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            volumeSettingsContentObserver)
    }

    override suspend fun changeVolume(volume: Int) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentVolume() = callbackFlow {

        volumeSettingsContentObserver.onCurrentVolumeChanged { currentVolume ->
            trySend(currentVolume)
        }
        awaitClose {
            volumeSettingsContentObserver.destroyVolumeCallback()
        }
    }

    override fun getMaxVolume(): Int =
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    override fun onDestroy() {
        context.contentResolver.unregisterContentObserver(volumeSettingsContentObserver)
    }
}