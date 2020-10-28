package com.dhimandasgupta.myjetpacknews.ui.common

import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage

private const val OFFSET_INIT_STATE = "offset_init"
private const val OFFSET_TO_STATE = "offset_to"

private const val SCALE_INIT_STATE = "scale_init"
private const val SCALE_TO_STATE = "scale_to"

private val offSet = FloatPropKey()

private val offSetTransitionDefinition = transitionDefinition<String> {
    state(OFFSET_INIT_STATE) { this[offSet] = 5.0f }
    state(OFFSET_TO_STATE) { this[offSet] = 2.0f }

    transition(fromState = OFFSET_INIT_STATE, toState = OFFSET_TO_STATE) {
        offSet using repeatable(
            animation = tween<Float>(
                durationMillis = 10000,
                easing = LinearOutSlowInEasing
            ),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse
        )
    }
}

private val scale = FloatPropKey()

private val scaleTransitionDefinition = transitionDefinition<String> {
    state(SCALE_INIT_STATE) { this[scale] = 2.0f }
    state(SCALE_TO_STATE) { this[scale] = 1.0f }

    transition(fromState = SCALE_INIT_STATE, toState = SCALE_TO_STATE) {
        scale using repeatable(
            animation = tween<Float>(
                durationMillis = 10000,
                easing = LinearOutSlowInEasing,
            ),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse
        )
    }
}

@Composable
fun KenBurns(url: String) {
    Box(modifier = Modifier.fillMaxSize(1f).wrapContentSize(align = Alignment.Center)) {
        val stateOffsetX = transition(
            definition = offSetTransitionDefinition,
            initState = OFFSET_INIT_STATE,
            toState = OFFSET_TO_STATE
        )

        val stateScale = transition(
            definition = scaleTransitionDefinition,
            initState = SCALE_INIT_STATE,
            toState = SCALE_TO_STATE
        )

        CoilImage(
            data = url,
            loading = {
                CircularProgressIndicator()
            },
            contentScale = ContentScale.None,
            onRequestCompleted = {
                // May be start here
            },
            /*getSuccessPainter = {
              ImagePainter(
                  image = it.image,
                  srcOffset = IntOffset((it.image.width /stateOffsetX[offSet]).toInt(), (it.image.height /stateOffsetX[offSet]).toInt()),
                  srcSize = IntSize((it.image.width * stateScale[scale]).toInt(), (it.image.height * stateScale[scale]).toInt())
              )
            },*/
            modifier = Modifier.width(400.dp).height(400.dp).clipToBounds()
        )
    }
}

private data class ScaleOffSetValue(
    val scaleFrom: Float = 1.0f,
    val scaleTo: Float = 2.0f,
    val offSetFrom: Offset = Offset(0.0f, 0.0f),
    val offSetT: Offset = Offset(1.0f, 1.0f)
)
