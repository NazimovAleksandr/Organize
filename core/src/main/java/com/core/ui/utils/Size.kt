package com.core.ui.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dp.dpToPx(): Float = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun keyboardHeight(): Int = WindowInsets.ime.getBottom(LocalDensity.current)

@Composable
fun keyboardHeightWithoutNavBar(): Dp {
    val keyboardHeight = keyboardHeight()
    val bottomBarHeight = bottomBarHeight.dpToPx().toInt()

    var paddingBottom =
        if (keyboardHeight > bottomBarHeight) {
            keyboardHeight - bottomBarHeight
        } else {
            0
        }

    if (paddingBottom < 0) paddingBottom = 0

    return paddingBottom.pxToDp()
}

var bottomBarHeight: Dp = 0.dp
