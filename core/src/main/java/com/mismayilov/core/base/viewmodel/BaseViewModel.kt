package com.mismayilov.core.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismayilov.domain.remote.ResultWrapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

abstract class BaseViewModel<State, Event, Effect>: ViewModel() {
    private val viewState: State by lazy { getInitialState() }
    abstract fun getInitialState(): State
    private val _state = MutableStateFlow(viewState)
    val state:StateFlow<State> get() = _state

    private val _event = MutableSharedFlow<Event>()
    val event:SharedFlow<Event> get() = _event

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        initEvents()
    }

    private fun initEvents() {
        _event
            .onEach {
                onEventChanged(it)
            }.launchIn(viewModelScope)

    }

    fun setState(state: State) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }
    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
    fun setEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    suspend fun <T> invoke(
        kSuspendFunction: suspend () -> T,
        onError: ((e: Exception) -> Unit)? = null,
        onSuccess: (T) -> Unit
    ) {
        when (val result = invokeRequest(kSuspendFunction)) {
            is ResultWrapper.Error -> onError?.invoke(result.exception)
            is ResultWrapper.Success -> onSuccess(result.value)
        }
    }

    private suspend fun <T> invokeRequest(kSuspendFunction: suspend () -> T): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(kSuspendFunction())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }


    open fun onEventChanged(event: Event) {}
    fun getCurrentState() = state.value
}
