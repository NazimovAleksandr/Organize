package com.organize.ka.bottom_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.ui.theme.OrganizeTheme
import com.core.nav_graph.AppNavGraph
import com.core.nav_graph.AppNavGraphImpl
import com.core.nav_graph.navigate

@Composable
internal fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        AppNavGraphImpl.BottomBarGraph.Tasks,
        AppNavGraphImpl.BottomBarGraph.Notes,
        AppNavGraphImpl.BottomBarGraph.Plans,
        AppNavGraphImpl.BottomBarGraph.Habits,
        AppNavGraphImpl.BottomBarGraph.Settings,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (items.map { (it as AppNavGraph).graphRoute }.contains(currentRoute)) {
        BottomBar(navController, items, currentRoute)
    }
}

@Composable
private fun BottomBar(navController: NavHostController, items: List<AppNavGraphImpl.BottomBarGraph>, currentRoute: String?) {
    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier

    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.icon),
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier
                        )

                        Text(
                            text = stringResource(id = item.title),
                        )
                    }
                },
                label = {},
                colors = NavigationBarItemDefaults.colors(
                    /*selectedIconColor = C1,
                    selectedTextColor = C1,
                    indicatorColor = C14,
                    unselectedIconColor = C5,
                    unselectedTextColor = C5,*/
                ),
                alwaysShowLabel = true,
                selected = currentRoute == (item as AppNavGraph).graphRoute,
                onClick = {
                    if ((item as AppNavGraph).graphRoute != currentRoute) {
                        navController.navigate(item as AppNavGraph)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            BottomBar(
                navController = rememberNavController(),
                items = listOf(
                    AppNavGraphImpl.BottomBarGraph.Tasks,
                    AppNavGraphImpl.BottomBarGraph.Notes,
                    AppNavGraphImpl.BottomBarGraph.Plans,
                    AppNavGraphImpl.BottomBarGraph.Habits,
                    AppNavGraphImpl.BottomBarGraph.Settings,
                ),
                currentRoute = AppNavGraphImpl.BottomBarGraph.Tasks.graphRoute
            )
        }
    }
}
