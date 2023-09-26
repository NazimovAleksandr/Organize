package com.screen.splash

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.core.mvi.contract.ScreenEvent
import com.core.mvi.contract.ScreenSingleEvent
import com.core.mvi.contract.ScreenState
import com.core.mvi.processor.MviProcessor
import com.organize.data.DataManager
import com.organize.entity.icons.Icons
import com.organize.entity.task_category.CategoryUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface SplashScreenState : ScreenState
interface SplashSplashEvent : ScreenEvent
interface SplashScreenSingleEvent : ScreenSingleEvent

class SplashViewModel(
    context: Context,
    dataManager: DataManager,
) : MviProcessor<SplashScreenState, SplashSplashEvent, SplashScreenSingleEvent>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (dataManager.getCategory().first().isEmpty()) {
                dataManager.setAllCategory(
                    category = listOf(
                        CategoryUI(id = 1, icon = Icons.Work, name = context.getString(com.res.R.string.category_work)),
                        CategoryUI(id = 2, icon = Icons.Education, name = context.getString(com.res.R.string.category_study)),
                        CategoryUI(id = 3, icon = Icons.Home, name = context.getString(com.res.R.string.category_home)),
                        CategoryUI(id = 4, icon = Icons.Me, name = context.getString(com.res.R.string.category_me)),
                    )
                )
            }
        }
    }

    override fun initialState(): SplashScreenState {
        return object : SplashScreenState {}
    }

    override fun reduce(event: SplashSplashEvent, state: SplashScreenState): SplashScreenState {
        return state
    }

    override suspend fun handleEvent(event: SplashSplashEvent, state: SplashScreenState): SplashSplashEvent? {
        return null
    }
}