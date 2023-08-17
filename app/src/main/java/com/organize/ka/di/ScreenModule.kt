package com.organize.ka.di

import com.screen.tasks.TasksViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val screenModule = module {
    viewModelOf(::TasksViewModel)
}