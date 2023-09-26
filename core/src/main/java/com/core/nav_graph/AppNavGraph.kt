package com.core.nav_graph

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog

fun NavGraphBuilder.composable(
    graphDestination: AppNavGraph,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = graphDestination.graphRoute,
        arguments = graphDestination.arguments,
        deepLinks = graphDestination.deepLinks,
        content = content
    )
}

fun NavGraphBuilder.dialog(
    graphDestination: AppNavGraph,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    dialog(
        route = graphDestination.graphRoute,
        arguments = graphDestination.arguments,
        deepLinks = graphDestination.deepLinks,
        content = content
    )
}

fun NavHostController.navigate(navDestination: AppNavGraph) {
    navigate(
        route = navDestination.navigateRoute,
        navOptions = navDestination.navOptions,
        navigatorExtras = navDestination.navigatorExtras,
    )
}

abstract class AppNavGraph {
    open val graphRoute: String get() = this::class.java.name
    open var navigateRoute: String = this::class.java.name
    open var navOptions: NavOptions? = null
    open var navigatorExtras: Navigator.Extras? = null

    /**
     * value = listOf(navArgument("userId") { defaultValue = "user1234" })
     * or = listOf(navArgument("userId") { type = NavType.StringType })
     */
    open val arguments: List<NamedNavArgument> = emptyList()

    /**
     * value = listOf(navDeepLink { uriPattern = "$uri/{id}" })
     */
    open val deepLinks: List<NavDeepLink> = emptyList()
}