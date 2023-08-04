package com.screen.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.nav_graph.AppNavGraphImpl
import com.core.nav_graph.composable
import com.core.nav_graph.navigate
import com.screen.splash.SplashScreen

fun NavGraphBuilder.splashScreen(navController: NavHostController) {
    composable(
        graphDestination = AppNavGraphImpl.Splash,
        content = { SplashScreen(navController::navigate) }
    )
}