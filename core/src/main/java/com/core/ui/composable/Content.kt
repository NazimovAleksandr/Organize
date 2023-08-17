package com.core.ui.composable

import androidx.compose.runtime.Composable

@Composable
fun Content(content: @Composable () -> Unit) {
    content.invoke()
}
