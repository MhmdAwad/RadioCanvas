package com.mhmdawad.radu.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2

object Utils {

    fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
        val (dx, dy) = currentPosition - center
        val theta = atan2(dy, dx).toDouble()

        var angle = Math.toDegrees(theta)
        if (angle < 0) angle += 360.0
        if (angle < 10f) angle = 10.0
        if (angle > 170) angle = 170.0
        return angle
    }

}