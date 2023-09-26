package com.organize.ka.di

import androidx.room.Room
import com.organize.data.DataManager
import com.organize.data.data_base.AppDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::DataManager)

    single {
        Room
            .databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .build()
    }
}