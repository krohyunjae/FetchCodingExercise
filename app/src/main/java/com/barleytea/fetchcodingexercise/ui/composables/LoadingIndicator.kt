package com.barleytea.fetchcodingexercise.ui.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barleytea.fetchcodingexercise.ui.theme.basePadding

enum class AnimationType {
    Bounce,
    Fade,
}
private const val IndicatorSize = 12
private const val AnimationDurationMillis = 300
private const val NumIndicators = 3
private const val AnimationDelayMillis = AnimationDurationMillis / NumIndicators

@Composable
fun BoxScope.LoadingIndicator(
    modifier: Modifier = Modifier,
    animating: Boolean = true,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    indicatorSpacing: Dp = basePadding/2,
    // 1
    animationType: AnimationType = AnimationType.Bounce,
) {
    val animatedValues = List(NumIndicators) { index ->
        // 2
        var animatedValue by remember(key1 = animating, key2 = animationType) { mutableFloatStateOf(0f) }
        // 3
        LaunchedEffect(key1 = animating, key2 = animationType) {
            if (animating) {
                // 4
                animate(
                    initialValue = IndicatorSize / 2f,
                    targetValue = -IndicatorSize / 2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = AnimationDurationMillis),
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(AnimationDelayMillis * index),
                    ),
                ) { value, _ -> animatedValue = value }
            }
        }
        animatedValue
    }
    Row(modifier = modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
        animatedValues.forEach { animatedValue ->
            LoadingDot(
                modifier = Modifier
                    .padding(horizontal = indicatorSpacing)
                    .width(IndicatorSize.dp)
                    .aspectRatio(1f)
                    .then(
                        //5
                        when (animationType) {
                            AnimationType.Bounce -> Modifier.offset(y = animatedValue.dp)
                            AnimationType.Fade -> Modifier.graphicsLayer { alpha = animatedValue }
                        }
                    ),
                color = color,
            )
        }
    }
}


@Composable
private fun LoadingDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color)
    )
}