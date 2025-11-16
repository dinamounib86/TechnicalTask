package com.genwin.istoria.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerListItem(
    itemHeight: Dp = 60.dp,
    itemCount: Int = 3,
    padding: PaddingValues = PaddingValues(16.dp),
    color: Color = Color.LightGray,
    cornerRadius: Dp = 4.dp
) {
    val shimmerColors = listOf(
        color.copy(alpha = 0.6f),
        color.copy(alpha = 0.2f),
        color.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Column(modifier = Modifier.padding(padding)) {
        repeat(itemCount) {
            ShimmerItem(
                colors = shimmerColors,
                xShimmer = translateAnim.value,
                yShimmer = translateAnim.value,
                itemHeight = itemHeight,
                cornerRadius = cornerRadius
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ShimmerItem(
    colors: List<Color>,
    xShimmer: Float,
    yShimmer: Float,
    itemHeight: Dp,
    cornerRadius: Dp
) {
    val brush = Brush.linearGradient(
        colors,
        start = Offset(xShimmer - 200, yShimmer - 200),
        end = Offset(xShimmer, yShimmer)
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(brush)
        )
    }
}
