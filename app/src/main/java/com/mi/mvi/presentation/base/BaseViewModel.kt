package com.mi.mvi.presentation.base

import androidx.lifecycle.*
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<EventState, ViewState> : ViewModel() {
    private val dataChannel: ConflatedBroadcastChannel<DataState<ViewState>> =
        ConflatedBroadcastChannel()

    // keep this protected so that only the ViewModel can modify the state
    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    // Create a publicly accessible LiveData object that can be observed
    val viewState: LiveData<ViewState>
        get() =  _viewState

    // keep this protected so that only the ViewModel can modify the state
    protected val _dataState: MutableLiveData<DataState<ViewState>> = MutableLiveData()
    // Create a publicly accessible LiveData object that can be observed
    val dataState: LiveData<DataState<ViewState>> = _dataState

    init {
        dataChannel
            .asFlow()
            .onEach { dataState ->
               _dataState.value = dataState
            }
            .launchIn(viewModelScope)
    }

    fun setEventState(eventState: EventState) {
        dataChannel.let { channel ->
            handleEventState(eventState).asFlow().onEach { data ->
                if (!channel.isClosedForSend) {
                    channel.offer(data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun getCurrentViewStateOrNew(): ViewState {
        return this.viewState.value?.let {
            it
        } ?: initNewViewState()
    }

    abstract fun handleEventState(eventState: EventState): LiveData<DataState<ViewState>>

    abstract fun initNewViewState(): ViewState
}