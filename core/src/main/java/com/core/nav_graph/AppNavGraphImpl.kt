package com.core.nav_graph

import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions

interface AppNavGraphImpl {
    object Splash : AppNavGraph()

    interface BottomBarGraph {
        val title: Int
        val icon: Int

        val bottomNavOptions: NavOptions
            get() = getNavOptions {
                popUpTo(Tasks.graphRoute) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }

        private fun getNavOptions(builder: NavOptionsBuilder.() -> Unit): NavOptions {
            return navOptions(builder)
        }

        object Tasks : AppNavGraph(), BottomBarGraph {
            override val title = com.res.R.string.tasks
            override val icon = com.res.R.drawable.ic_tasks

            override var navOptions: NavOptions? = bottomNavOptions
        }

        object Notes : AppNavGraph(), BottomBarGraph {
            override val title = com.res.R.string.notes
            override val icon = com.res.R.drawable.ic_notes

            override var navOptions: NavOptions? = bottomNavOptions
        }

        object Plans : AppNavGraph(), BottomBarGraph {
            override val title = com.res.R.string.plans
            override val icon = com.res.R.drawable.ic_plans

            override var navOptions: NavOptions? = bottomNavOptions
        }

        object Habits : AppNavGraph(), BottomBarGraph {
            override val title = com.res.R.string.habits
            override val icon = com.res.R.drawable.ic_habits

            override var navOptions: NavOptions? = bottomNavOptions
        }

        object Settings : AppNavGraph(), BottomBarGraph {
            override val title = com.res.R.string.settings
            override val icon = com.res.R.drawable.ic_settings

            override var navOptions: NavOptions? = bottomNavOptions
        }
    }
}