package com.mi.mvi.base

import androidx.lifecycle.*
import com.mi.mvi.domain.datastate.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<EventState, ViewState> : ViewModel() {
    private val dataChannel: ConflatedBroadcastChannel<DataState<ViewState>> =
        ConflatedBroadcastChannel()

    // keep this protected so that only the ViewModel can modify the state
    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    // Create a publicly accessible LiveData object that can be observed
    val viewState: LiveData<ViewState>
        get() = _viewState

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
            handleEventState(eventState).onEach { data ->
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
        return this.viewState.value ?: initNewViewState()
    }

    abstract fun handleEventState(eventState: EventState): Flow<DataState<ViewState>>

    abstract fun initNewViewState(): ViewState
}
