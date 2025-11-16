package com.genwin.istoria.composables

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aroundegypt.R

@Composable
fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = colorResource(R.color.black),//Color.White,
    strokeWidth: Dp = 2.dp,
    trackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
) {
    CircularProgressIndicator(
        modifier = modifier.wrapContentSize(),
        color = color,
        strokeWidth = strokeWidth,
        trackColor = trackColor,
        strokeCap = strokeCap
    )
}
