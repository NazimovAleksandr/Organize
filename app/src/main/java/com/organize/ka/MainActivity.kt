package com.organize.ka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.core.ui.theme.OrganizeTheme
import com.core.nav_graph.AppNavGraphImpl
import com.organize.ka.bottom_bar.AppBottomBar
import com.organize.ka.navigation.appNavGraphBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent { App() }
    }

    @Composable
    private fun App() {
        val navController = rememberNavController()

        OrganizeTheme {
            Scaffold(
                bottomBar = { AppBottomBar(navController = navController) }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = AppNavGraphImpl.Splash.graphRoute,
                    builder = { appNavGraphBuilder(navController = navController) },
                    modifier = Modifier
                        .padding(
                            bottom = paddingValues.calculateBottomPadding()
                        )
                )
            }
        }
    }
}