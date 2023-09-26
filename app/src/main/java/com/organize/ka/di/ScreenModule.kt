package com.organize.ka.di

import com.dialog.newtask.NewTaskViewModel
import com.screen.splash.SplashViewModel
import com.screen.tasks.TasksViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val screenModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::TasksViewModel)

    factoryOf(::NewTaskViewModel)
}