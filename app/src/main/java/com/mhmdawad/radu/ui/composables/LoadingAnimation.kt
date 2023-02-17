package com.mhmdawad.radu.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*


@Composable
fun LoadingAnimation(
    size: Dp,
    color: Color,
    rawFile: Int,
    modifier: Modifier = Modifier,
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawFile))
    val lottieDynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = color.toArgb(),
            keyPath = arrayOf(
                "**",
            )
        )
    )

    LottieAnimation(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        dynamicProperties = lottieDynamicProperties,
        speed = .6f
    )
}