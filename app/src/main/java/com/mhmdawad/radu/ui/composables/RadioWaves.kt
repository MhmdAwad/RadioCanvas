package com.mhmdawad.radu.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mhmdawad.radu.domain.model.WavesOffset
import com.mhmdawad.radu.utils.addNewWave
import com.mhmdawad.radu.utils.leftShift
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun RadioWaves(
    isPlaying: Boolean,
    height: Dp = 50.dp,
    activeStrokeWidth: Dp = 1.2.dp,
    inActiveStrokeWidth: Dp = 0.5.dp,
    wavesTime: Long = 200,
    activeColor: Color = MaterialTheme.colors.primary,
    inActiveColor: Color = Color.Gray,
) {
    var wavesCount by remember { mutableStateOf(0) }
    var wavesOffset by remember { mutableStateOf(mutableListOf<WavesOffset>()) }
    var canvasHeight by remember { mutableStateOf(0f) }

    LaunchedEffect(wavesCount, canvasHeight) {
        wavesOffset.clear()
        repeat(wavesCount) {
            wavesOffset.addNewWave(canvasHeight)
        }
    }

    LaunchedEffect(isPlaying) {
        launch {
            while (isPlaying) {
                delay(wavesTime)
                wavesOffset = wavesOffset.leftShift(canvasHeight)
            }
        }
        for (offset in wavesOffset) {
            launch {
                val offsetPosition = if (isPlaying) offset.startYInPlay else offset.startYInStop
                offset.anim.startOffsetAnim.animateTo(
                    targetValue = offsetPosition,
                    animationSpec = keyframes {
                        durationMillis = 600
                        (offset.startYInStop).at(200).with(LinearEasing)
                        (offset.startYInPlay).at(350).with(LinearEasing)
                        (offset.startYInStop).at(500).with(LinearEasing)
                    }
                )
            }
            launch {
                val offsetPosition = if (isPlaying) offset.endYInPlay else offset.endYInStop
                offset.anim.endOffsetAnim.animateTo(
                    targetValue = offsetPosition,
                    animationSpec = keyframes {
                        durationMillis = 600
                        (offset.endYInStop).at(200).with(LinearEasing)
                        (offset.endYInPlay).at(350).with(LinearEasing)
                        (offset.endYInStop).at(500).with(LinearEasing)
                    })
            }
        }

    }

    Canvas(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        wavesCount = size.width.toInt() / 6
        canvasHeight = size.height
        wavesOffset.forEachIndexed { index, it ->
            val isInCenter = index in (wavesCount * .3).toInt()..(wavesCount * .7).toInt()
            val x = (index * 4f * 1.5f)

            val startOffset = Offset(
                x,
                it.anim.startOffsetAnim.value
            )
            val endOffset = Offset(
                x,
                it.anim.endOffsetAnim.value
            )

            drawLine(
                color = if (isInCenter) activeColor else inActiveColor,
                start = startOffset,
                end = endOffset,
                strokeWidth = if (isInCenter) activeStrokeWidth.toPx() else inActiveStrokeWidth.toPx()
            )
        }
    }
}