package com.mhmdawad.radu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.radu.utils.AudioHelper
import com.mhmdawad.radu.utils.AudioHelperImpl
import com.mhmdawad.radu.utils.launchWithIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val audioHelper: AudioHelper,
) : ViewModel() {

    private val _currentVolumeState = MutableStateFlow(0)
    val currentVolumeState: StateFlow<Int> = _currentVolumeState

    init {
        getCurrentVolume()
    }

    fun changeVolume(volume: Int) {
        launchWithIO {
            audioHelper.changeVolume(volume)
        }
    }

    private fun getCurrentVolume() {
        launchWithIO {
            audioHelper.getCurrentVolume()
                .collect {
                    _currentVolumeState.value = it
                }
        }
    }

    fun getMaxVolume() =  audioHelper.getMaxVolume()

    override fun onCleared() {
        super.onCleared()
        audioHelper.onDestroy()
    }
}