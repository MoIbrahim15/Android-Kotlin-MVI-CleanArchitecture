package com.mi.mvi.ui.main.blog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.mi.mvi.data.models.BlogPost
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
                    viewState.value?.blogsFields?.searchQuery?.let { query ->
                        searchBlogUseCase.invoke(authToken, query)
                    }
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.None -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setQuery(query: String) {
        val update = getCurrentViewStateOrNew()
//        if (query != update.blogsFields.searchQuery) {
            update.blogsFields.searchQuery = query
            _viewState.value = update
//        }
    }

    fun setBlogList(blogList: MutableList<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogsFields.blogList = blogList
        _viewState.value = update
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }
}