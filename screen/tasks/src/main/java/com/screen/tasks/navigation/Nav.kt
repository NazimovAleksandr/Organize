package com.screen.tasks.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.nav_graph.AppNavGraphImpl
import com.core.nav_graph.composable
import com.core.nav_graph.navigate
import com.screen.tasks.TasksScreen

fun NavGraphBuilder.tasksScreen(navController: NavHostController) {
    composable(
        graphDestination = AppNavGraphImpl.BottomBarGraph.Tasks,
        content = { TasksScreen(navController::navigate) }
    )
}