package com.organize.ka.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.screen.splash.navigation.splashScreen

fun NavGraphBuilder.appNavGraphBuilder(navController: NavHostController) {
    splashScreen(navController = navController)
}