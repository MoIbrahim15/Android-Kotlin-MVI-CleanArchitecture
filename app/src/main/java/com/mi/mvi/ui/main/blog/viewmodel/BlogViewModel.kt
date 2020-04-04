package com.mi.mvi.ui.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.domain.main.blogs.SearchBlogUseCase
import com.mi.mvi.ui.BaseViewModel
import com.mi.mvi.ui.main.blog.state.BlogEventState
import com.mi.mvi.ui.main.blog.state.BlogViewState
import com.mi.mvi.utils.AbsentLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BlogViewModel(
    private val searchBlogUseCase: SearchBlogUseCase,
    private val sessionManager: SessionManager,
    private val sheredPreferences: SharedPreferences,
    private val requestManager: RequestManager
) : BaseViewModel<BlogEventState, BlogViewState>() {

    override fun handleEventState(eventState: BlogEventState): LiveData<DataState<BlogViewState>> {
        return when (eventState) {
            is BlogEventState.BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    searchBlogUseCase.invoke(
                        token = authToken,
                        query = getSearchQuery(),
                        page = getPage()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.CheckAuthorBlogPostEvent -> {
                AbsentLiveData.create()
            }
            is BlogEventState.None -> {
                object : LiveData<DataState<BlogViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.LOADING(false)
                    }
                }
            }
        }
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }
}