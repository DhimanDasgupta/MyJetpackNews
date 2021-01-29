package com.dhimandasgupta.myjetpacknews.ui.common

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultCameraDistance
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ImagePainter
import androidx.compose.ui.layout.ContentScale
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// Unfinished, work to be done on this
// ToDo replace depricated apis
@Composable
fun KenBurns(
    data: Any,
    modifier: Modifier
) {
    val start = remember { mutableStateOf(KenBurnsState.Start) }
    val end = remember { mutableStateOf(KenBurnsState.End) }

    val transitionState = transition(
        definition = definition,
        initState = start.value,
        toState = end.value,
        onStateChangeFinished = {
            when (it) {
                KenBurnsState.Start -> {
                    start.value = KenBurnsState.End
                    end.value = KenBurnsState.Start
                }
                KenBurnsState.End -> {
                    start.value = KenBurnsState.End
                    end.value = KenBurnsState.Start
                }
            }
        }
    )

    CoilImage(
        data = data,
        modifier = modifier.then(
            Modifier
                .graphicsLayer(
                    scaleX = transitionState[scaleXProp],
                    scaleY = transitionState[scaleYProp],
                    cameraDistance = 10.0f,
                    transformOrigin = TransformOrigin(0.5f, 0.25f),
                    translationX = transitionState[translationXProp],
                    translationY = transitionState[translationYProp],
                )
        ),
        contentDescription = "",
        contentScale = ContentScale.None,
        fadeIn = true,
        onRequestCompleted = { imageLoadState ->
            if (imageLoadState is ImageLoadState.Success) {
                val width = imageLoadState.painter.intrinsicSize.width
                val height = imageLoadState.painter.intrinsicSize.height

                val maxDimension = imageLoadState.painter.intrinsicSize.maxDimension
                val minDimension = imageLoadState.painter.intrinsicSize.minDimension

                val painter = imageLoadState.painter as ImagePainter

                Log.d("Hello", "Width: $width")
                Log.d("Hello", "Height: $height")
                Log.d("Hello", "Max Dimension: $maxDimension")
                Log.d("Hello", "Min Dimension: $minDimension")
            }
        },
    )

    val scope = rememberCoroutineScope()

    onActive(
        callback = {
            scope.launch {
                while (isActive) {
                    delay(1000)
                    start.value = KenBurnsState.End
                    end.value = KenBurnsState.Start
                }
            }
        }
    )
}

private val scaleXProp = FloatPropKey(label = "scaleX")
private val scaleYProp = FloatPropKey(label = "scaleY")
private val translationXProp = FloatPropKey(label = "translationX")
private val translationYProp = FloatPropKey(label = "translationY")

private val definition = transitionDefinition<KenBurnsState> {
    state(KenBurnsState.Start) {
        this[scaleXProp] = 1f
        this[scaleYProp] = 1f
        this[translationXProp] = 0f
        this[translationYProp] = 0f
    }
    state(KenBurnsState.End) {
        this[scaleXProp] = 1.5f
        this[scaleYProp] = 1.5f
        this[translationXProp] = 1.25f
        this[translationYProp] = 1.5f
    }

    transition(
        KenBurnsState.Start to KenBurnsState.End,
        KenBurnsState.End to KenBurnsState.Start
    ) {
        scaleXProp using tween(
            durationMillis = 1,
            easing = FastOutSlowInEasing,
        )

        scaleYProp using tween(
            durationMillis = 1,
            easing = FastOutLinearInEasing,
        )

        translationXProp using tween(
            durationMillis = 5000,
            easing = FastOutLinearInEasing,
        )

        translationYProp using tween(
            durationMillis = 5000,
            easing = FastOutLinearInEasing,
        )
    }
}

enum class KenBurnsState {
    Start, End
}

private data class KenBurnsProps(
    val scaleX: Float,
    val scaleY: Float,
    val cameraDistance: Float,
    val transformOrigin: TransformOrigin,
    val translationX: Float,
    val translationY: Float,
    val clip: Boolean = false
)

private val initialProps = KenBurnsProps(
    scaleX = 1f,
    scaleY = 1f,
    translationX = 0f,
    translationY = 0f,
    cameraDistance = DefaultCameraDistance,
    transformOrigin = TransformOrigin.Center,
    clip = false
)

private val endProps = KenBurnsProps(
    scaleX = 3f,
    scaleY = 2.5f,
    cameraDistance = 10.0f,
    transformOrigin = TransformOrigin(0.5f, 0.25f),
    translationX = 0.25f,
    translationY = 0.2f,
    clip = false
)
