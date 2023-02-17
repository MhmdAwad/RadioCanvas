package com.mhmdawad.radu.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mhmdawad.radu.utils.Utils
import java.lang.Float.max
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun RadioKnob(
    canvasSize: Dp,
    angle: Double,
    degree: Double,
    maxIndicatorValue: Float,
    onVolumeColorAlphaChange: (Float) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onAngleChange: (Double) -> Unit,
    knobRadius: Dp = 10.dp,
    outKnobColor: Color = MaterialTheme.colors.primary,
    inKnobColor: Color = Color.Gray,
) {
    var handleCenter by remember { mutableStateOf(Offset.Zero) }
    var shapeCenter by remember { mutableStateOf(Offset.Zero) }
    val angleInDegrees by remember { mutableStateOf(degree) }.apply {
        value = degree
    }

    Canvas(modifier = Modifier
        .size(canvasSize)
        .pointerInput(Unit) {
            detectDragGestures(onDrag = { change, dragAmount ->
                handleCenter += dragAmount
                onAngleChange(Utils.getRotationAngle(handleCenter, shapeCenter))
                val progress = (angleInDegrees / 100) * maxIndicatorValue
                onProgressChanged((progress).roundToInt())
                change.consumeAllChanges()
            },
                onDragEnd = {

                })
        },
        onDraw = {
            shapeCenter = center
            val radius = size.minDimension * .4f
            val x = (shapeCenter.x + cos(Math.toRadians(angle)) * radius).toFloat()
            val y = (shapeCenter.y + sin(Math.toRadians(angle)) * radius).toFloat()
            handleCenter = Offset(x, y)
            onVolumeColorAlphaChange(max((angleInDegrees / 100).toFloat(), 0.15f))

            drawCircle(
                color = outKnobColor,
                radius = knobRadius.toPx(),
                center = handleCenter
            )

            drawCircle(
                color = inKnobColor,
                radius = knobRadius.toPx() / 2,
                center = handleCenter
            )
        }
    )
}
