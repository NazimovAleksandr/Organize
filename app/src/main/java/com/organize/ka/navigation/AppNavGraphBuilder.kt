package com.organize.ka.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.screen.splash.navigation.splashScreen
import com.screen.tasks.navigation.tasksScreen

fun NavGraphBuilder.appNavGraphBuilder(navController: NavHostController) {
    splashScreen(navController = navController)
    tasksScreen(navController = navController)
}