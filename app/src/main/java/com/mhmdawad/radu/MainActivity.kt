package com.mhmdawad.radu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mhmdawad.radu.ui.composables.RadioVolumeController
import com.mhmdawad.radu.ui.composables.RadioWaves
import com.mhmdawad.radu.ui.theme.RaduTheme
import com.mhmdawad.radu.utils.noRippleClickable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var isPlaying by remember {
                mutableStateOf(true)
            }

            RaduTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 32.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            RadioVolumeController(
                                "https://countryflagsapi.com/png/egpt",
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RadioWaves(
                                isPlaying
                            )

                            Spacer(modifier = Modifier.height(50.dp))

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.primary)
                                    .noRippleClickable { isPlaying = !isPlaying },
                                contentAlignment = Alignment.Center) {

                                Icon(
                                    modifier = Modifier
                                        .size(32.dp),
                                    tint = Color.White,
                                    painter = painterResource(
                                        id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                                    ),
                                    contentDescription = "play_icon")
                            }

                        }

                    }
                }
            }
        }
    }
}




