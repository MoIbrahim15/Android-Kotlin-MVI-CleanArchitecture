package com.mi.mvi.ui.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mi.mvi.data.preference.SharedPreferenceKeys.Companion.BLOG_FILTER
import com.mi.mvi.data.preference.SharedPreferenceKeys.Companion.BLOG_ORDER
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.domain.main.blogs.DeleteBlogPostUseCase
import com.mi.mvi.domain.main.blogs.IsAuthorBlogPostUseCase
import com.mi.mvi.domain.main.blogs.SearchBlogUseCase
import com.mi.mvi.ui.BaseViewModel
import com.mi.mvi.ui.main.blog.state.BlogEventState
import com.mi.mvi.ui.main.blog.state.BlogViewState
import com.mi.mvi.utils.AbsentLiveData
import com.mi.mvi.utils.BlogQueryUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BlogViewModel(
    private val searchBlogUseCase: SearchBlogUseCase,
    private val isAuthorBlogPostUseCase: IsAuthorBlogPostUseCase,
    private val deleteBlogPostUseCase: DeleteBlogPostUseCase,
    private val sessionManager: SessionManager,
    private val sheredPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BaseViewModel<BlogEventState, BlogViewState>() {

    init {
        setFilter(
            sheredPreferences.getString(
                BLOG_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            )
        )

        setOrder(
            sheredPreferences.getString(
                BLOG_ORDER,
                BlogQueryUtils.BLOG_ORDER_ASC
            )
        )
    }

    override fun handleEventState(eventState: BlogEventState): LiveData<DataState<BlogViewState>> {
        return when (eventState) {
            is BlogEventState.BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    searchBlogUseCase.invoke(
                        token = authToken,
                        query = getSearchQuery(),
                        filterAndOrder = getOrder() + getFilter(),
                        page = getPage()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.CheckAuthorBlogPostEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    isAuthorBlogPostUseCase.invoke(
                        token = authToken,
                        slug = getSlug()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.DeleteBlogPostEvent ->{
                sessionManager.cachedToken.value?.let { authToken ->
                    deleteBlogPostUseCase.invoke(
                        token = authToken,
                        blogPost = getBlogPost()
                    )
                } ?: AbsentLiveData.create()
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

    fun saveFilterOptions(filter: String, order: String) {
        editor.putString(BLOG_FILTER, filter)
        editor.apply()

        editor.putString(BLOG_ORDER, order)
        editor.apply()
    }
}