package com.example.aroundegypt.Utils.composables

import android.R
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImagePainter.State
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    crossFadeDuration: Int = 400,
    loadingShimmerColor: Color = Gray,
    errorPlaceholder: Painter? = painterResource(R.drawable.sym_contact_card),
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    loading: @Composable (SubcomposeAsyncImageScope.(State.Loading) -> Unit)? = { AsyncImageShimmer(loadingShimmerColor) },
    success: @Composable (SubcomposeAsyncImageScope.(State.Success) -> Unit)? = null,
    error: @Composable (SubcomposeAsyncImageScope.(State.Error) -> Unit)? = {
        if (errorPlaceholder != null) {
            Image(
                painter = errorPlaceholder,
                contentDescription = "Image loading fail",
                contentScale = ContentScale.Fit
            )
        }
    }
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = if (model.toString().contains("svg")) {
            ImageRequest.Builder(LocalContext.current)
                .data(model)
                .decoderFactory(SvgDecoder.Factory())
                .build()
        }else if (model is String) {
            ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(crossFadeDuration)
                .build()
        } else {
            model
        },
        contentDescription = "Image",
        imageLoader = imageLoader,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        loading = loading,
        success = success,
        error = error
    )
}

@Composable
private fun AsyncImageShimmer(loadingShimmerColor: Color) {
    val shimmerColors = listOf(
        Color.Transparent,
        loadingShimmerColor.copy(alpha = 0.6f),
        Color.Transparent
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

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 200, translateAnim.value - 200),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    )
}
