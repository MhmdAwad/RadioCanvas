package com.mhmdawad.radu.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.radu.domain.model.OffsetAnim
import com.mhmdawad.radu.domain.model.WavesOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


fun ViewModel.launchWithIO(invoke: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        invoke()
    }
}

inline fun <reified T> MutableList<T>.leftShift(height: Float): MutableList<T> {
    val newList = mutableListOf<T>()
    for (index in 1 until size)
        newList.add(get(index))

    return newList.apply { addNewWave(height) }
}

inline fun <reified T> MutableList<T>.addNewWave(height: Float) {
    val startYInPlay = height * (Random.nextDouble(0.15, 0.45).toFloat())
    val endYInPlay = height - startYInPlay
    val startYInStop = (height / 2) - 2f
    val endYInStop = (height / 2) + 2f
    if (T::class == WavesOffset::class)
        add(WavesOffset(
            startYInPlay, endYInPlay, startYInStop, endYInStop,
            OffsetAnim(Animatable(startYInPlay), Animatable(endYInPlay))
        ) as T)
}

fun Modifier.noRippleClickable(onClick: () -> Unit) = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}