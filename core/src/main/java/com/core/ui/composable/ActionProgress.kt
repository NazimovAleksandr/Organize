package com.core.ui.composable

import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.ui.theme.OrganizeTheme

@Composable
fun ActionProgress(
    @DrawableRes icon: Int,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 2.dp,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .size(size = 40.dp)
        )

        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(size = 30.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .padding(all = 3.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        ActionProgress(
            icon = com.core.R.drawable.ic_search,
            progress = 0.3f
        )
    }
}