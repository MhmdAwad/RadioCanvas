package com.mhmdawad.radu.domain.model

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D

data class WavesOffset(
    val startYInPlay: Float,
    val endYInPlay: Float,
    val startYInStop: Float,
    val endYInStop: Float,
    val anim: OffsetAnim
)

data class OffsetAnim(
    val startOffsetAnim: Animatable<Float, AnimationVector1D> = Animatable(0f),
    val endOffsetAnim: Animatable<Float, AnimationVector1D> = Animatable(0f),
)