package com.screen.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.core.nav_graph.AppNavGraph
import com.core.ui.theme.OrganizeTheme

@Composable
internal fun TasksScreen(
    navigate: (AppNavGraph) -> Unit,
) {
    Screen(navigate = navigate)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    navigate: (AppNavGraph) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "123",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = { -> },
            actions = { -> },
//            windowInsets = ,
            // scrollBehavior = null // TODO:
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    OrganizeTheme {
        Screen {}
    }
}