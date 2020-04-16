package com.mi.mvi.presentation.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mi.mvi.domain.usecase.blogs.*
import com.mi.mvi.presentation.BaseViewModel
import com.mi.mvi.presentation.main.blog.state.BlogEventState
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.AbsentLiveData
import com.mi.mvi.utils.BlogQueryUtils
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.SharedPreferenceKeys.Companion.BLOG_FILTER
import com.mi.mvi.utils.SharedPreferenceKeys.Companion.BLOG_ORDER
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@ExperimentalCoroutinesApi
class BlogViewModel(
    private val searchBlogUseCase: SearchBlogUseCase,
    private val restoreBlogListUSeCase: RestoreBlogListUseCase,
    private val isAuthorBlogPostUseCase: IsAuthorBlogPostUseCase,
    private val deleteBlogPostUseCase: DeleteBlogPostUseCase,
    private val updateBlogPostUseCase: UpdateBlogPostUseCase,
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
                clearLayoutManagerState()
                sessionManager.cachedToken.value?.let { authToken ->
                    searchBlogUseCase.invoke(
                        token = authToken,
                        query = getSearchQuery(),
                        filterAndOrder = getOrder() + getFilter(),
                        page = getPage()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.RestoreBlogListFromCacheEvent -> {
                restoreBlogListUSeCase.invoke(
                    query = getSearchQuery(),
                    filterAndOrder = getOrder() + getFilter(),
                    page = getPage()
                )
            }
            is BlogEventState.CheckAuthorBlogPostEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    isAuthorBlogPostUseCase.invoke(
                        token = authToken,
                        slug = getSlug()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.DeleteBlogPostEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    deleteBlogPostUseCase.invoke(
                        token = authToken,
                        blogPost = getBlogPost()
                    )
                } ?: AbsentLiveData.create()
            }

            is BlogEventState.UpdateBlogPostEvent -> {

                return sessionManager.cachedToken.value?.let { authToken ->

                    val title = eventState.title
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val body = eventState.body
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    updateBlogPostUseCase.invoke(
                        authToken = authToken,
                        slug = getSlug(),
                        title = title,
                        body = body,
                        image = eventState.image
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