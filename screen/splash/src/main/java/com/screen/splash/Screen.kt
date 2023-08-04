package com.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.nav_graph.AppNavGraph
import com.core.nav_graph.AppNavGraphImpl
import com.core.ui.theme.PriorityNotAssigned
import kotlinx.coroutines.delay

@Composable
internal fun SplashScreen(
    navigate: (AppNavGraph) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(weight = 100f))

        Image(
            imageVector = ImageVector.vectorResource(id = com.core.R.drawable.ic_splash_logo),
            contentDescription = null,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(weight = 40f))

        Text(
            text = stringResource(id = com.core.R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            text = stringResource(id = com.core.R.string.your_to_do_planner),
            style = MaterialTheme.typography.bodyLarge,
            color = PriorityNotAssigned,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(weight = 154f))
    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            delay(2000)
            navigate.invoke(AppNavGraphImpl.BottomBarGraph.Tasks)
        },
    )
}


