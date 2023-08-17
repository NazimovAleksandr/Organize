package com.core.mvi.processor

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.mvi.contract.Event
import com.core.mvi.contract.ScreenState
import com.core.mvi.contract.SingleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class MviProcessor<S : ScreenState, E : Event, SE : SingleEvent> : ViewModel() {

    @Suppress("PrivatePropertyName")
    private val TAG = this::class.java.simpleName

    protected abstract fun initialState(): S
    protected abstract fun reduce(event: E, state: S): S
    protected abstract suspend fun handleEvent(event: E, state: S): E?

    private val _initialState: S by lazy { initialState() }
    private val _uiState: MutableState<S> by lazy { mutableStateOf(_initialState) }
    val uiState: State<S> by lazy { _uiState }

    private val _event: MutableSharedFlow<E> = MutableSharedFlow()

    private val _singleEvent: MutableSharedFlow<SE> = MutableSharedFlow()
    val singleEvent: Flow<SE> = _singleEvent

    init {
        Log.d(TAG, "init")

        subscribeToEvent()
    }

    fun sendEvent(event: E) {
        Log.d(TAG, "sendEvent: event = $event")

        viewModelScope.launch {
            _event.emit(value = event)
        }
    }

    fun triggerSingleEvent(singleEvent: SE) {
        Log.d(TAG, "triggerSingleEvent: singleEvent = $singleEvent")

        viewModelScope.launch {
            _singleEvent.emit(singleEvent)
        }
    }

    private fun setState(state: S) {
        _uiState.value = state
    }

    private fun reduceInternal(event: E, state: S) {
        val newState = reduce(event, state)
        setState(newState)
    }

    private fun subscribeToEvent() {
        viewModelScope.launch {
            _event.collect { event ->
                Log.d(TAG, "subscribeToEvent: Collect event = $event")

                reduceInternal(event, _uiState.value)

                launch {
                    handleEvent(event, _uiState.value)?.let { newEvent -> sendEvent(newEvent) }
                }
            }
        }
    }
}