package com.mhmdawad.radu.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.mhmdawad.radu.MainViewModel
import com.mhmdawad.radu.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RadioVolumeController(
    imageUrl: String,
    mainViewModel: MainViewModel = hiltViewModel(),
    canvasSize: Dp = 300.dp,
    imageSize: Dp = 200.dp,
    loadingRaw: Int = R.raw.loading,
    loadingColor: Color = MaterialTheme.colors.primary
) {
    val indicatorValue by mainViewModel.currentVolumeState.collectAsState()

    val maxIndicatorValue by remember {
        mutableStateOf(mainViewModel.getMaxVolume().toFloat())
    }
    var angle by remember { mutableStateOf(0.0) }
    var angleInDegrees by remember { mutableStateOf(((170 - angle) / 160) * 100) }
    var volumeColorAlpha by remember { mutableStateOf(1f) }

    val transition = updateTransition(targetState = indicatorValue, label = "angle_transition")
    val animateAngle: Float by transition.animateFloat(
        transitionSpec = { tween(if (angle == 0.0) 500 else 0) }, label = "angle_transition",
    ) { state ->
        val percentage = (state / maxIndicatorValue) * 100
        (170 - (percentage * 1.6)).toFloat()
    }

    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(1000)
        }
    )

    LaunchedEffect(indicatorValue) { angle = 0.0 }

    Box {
        if (painter.state !is ImagePainter.State.Success)
            LoadingAnimation(
                size = imageSize,
                color = loadingColor,
                rawFile = loadingRaw,
                modifier = Modifier.align(Alignment.Center),
            )


        Image(
            painter = painter,
            contentDescription = stringResource(R.string.radio_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .align(Alignment.Center)
        )

        RadioSlider(
            canvasSize = canvasSize,
            volumeColorAlpha = volumeColorAlpha,
            angleInDegrees = angleInDegrees,
            allowedAngle = if (angle == 0.0) animateAngle else angle.toFloat(),
            onAngleInDegreesChange = { angleInDegrees = it }
        )

        RadioKnob(
            canvasSize = canvasSize,
            angle = if (angle == 0.0) animateAngle.toDouble() else angle,
            degree = angleInDegrees,
            maxIndicatorValue = maxIndicatorValue,
            onVolumeColorAlphaChange = { volumeColorAlpha = min(it, 1f) },
            onProgressChanged = { degree ->
                mainViewModel.changeVolume(degree)
            },
            onAngleChange = {
                angle = it
            },
        )
    }
}

@Composable
fun RadioSlider(
    canvasSize: Dp,
    volumeColorAlpha: Float,
    angleInDegrees: Double,
    allowedAngle: Float,
    color: Brush = Brush.sweepGradient(listOf(MaterialTheme.colors.primary,
        MaterialTheme.colors.secondary)),
    volumeColor: Color = MaterialTheme.colors.primary,
    arcWidth: Dp = 8.dp,
    visitedLineWidth: Dp = 2.dp,
    unvisitedLineWidth: Dp = 1.5.dp,
    onAngleInDegreesChange: (Double) -> Unit,
) {
    val muteIconPainter =
        rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.ic_mute))
    val volumeIconPainter =
        rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.ic_volume))
    val volumeTintColor by animateColorAsState(targetValue = volumeColor.copy(alpha = volumeColorAlpha))
    var arcSize by remember { mutableStateOf(Size.Zero) }

    Canvas(modifier = Modifier
        .size(canvasSize),
        onDraw = {
            arcSize = size / 1.25f
            val angleDegreeDifference = (360f / 60f)
            val radius = size.width * .46f
            onAngleInDegreesChange(((170 - allowedAngle.toDouble()) / 160) * 100)
            val muteIconPositionX = ((size.width - arcSize.width) / 2.5f)
            val volumeIconPositionX = muteIconPositionX + arcSize.width
            val positionY = arcSize.height / 1.7f

            drawArc(
                color = Color.LightGray,
                size = arcSize,
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(
                    x = (size.width - arcSize.width) / 2,
                    y = (size.height - arcSize.height) / 2,
                ),
                style = Stroke(
                    width = arcWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                brush = color,
                size = arcSize,
                startAngle = 170f,
                sweepAngle = allowedAngle - 170f,
                useCenter = false,
                topLeft = Offset(
                    x = (size.width - arcSize.width) / 2,
                    y = (size.height - arcSize.height) / 2,
                ),
                style = Stroke(
                    width = arcWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )

            translate(left = volumeIconPositionX, top = positionY) {
                with(volumeIconPainter) {
                    draw(
                        size = volumeIconPainter.intrinsicSize / 1.5f,
                        colorFilter = ColorFilter.tint(volumeTintColor)
                    )
                }
            }

            translate(left = muteIconPositionX, top = positionY) {
                with(muteIconPainter) {
                    draw(size = muteIconPainter.intrinsicSize / 1.5f)
                }
            }


            (2..28).forEach {
                val isVisited = angleInDegrees >= ((28 - it) / 26f) * 100
                val angleRadDifference =
                    (((angleDegreeDifference * it) - 360f) * (PI / 180f)).toFloat()
                val startOffsetLine = Offset(
                    x = (radius * .93f) * cos(angleRadDifference) + size.center.x,
                    y = (radius * .93f) * sin(angleRadDifference) + size.center.y
                )
                val endOffsetLine = Offset(
                    x = (radius - (radius * .05f)) * cos(angleRadDifference) + size.center.x,
                    y = (radius - (radius * .05f)) * sin(angleRadDifference) + size.center.y
                )

                drawLine(
                    brush = if (isVisited) color else Brush.linearGradient(listOf(
                        Color.Gray,
                        Color.LightGray)),
                    start = startOffsetLine,
                    end = endOffsetLine,
                    strokeWidth = if (isVisited) visitedLineWidth.toPx() else unvisitedLineWidth.toPx()
                )
            }
        })
}