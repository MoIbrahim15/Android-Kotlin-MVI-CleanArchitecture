package com.mi.mvi.presentation.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mi.mvi.domain.usecase.blogs.DeleteBlogPostUseCase
import com.mi.mvi.domain.usecase.blogs.IsAuthorBlogPostUseCase
import com.mi.mvi.domain.usecase.blogs.SearchBlogUseCase
import com.mi.mvi.domain.usecase.blogs.UpdateBlogPostUseCase
import com.mi.mvi.presentation.base.BaseViewModel
import com.mi.mvi.presentation.main.blog.state.BlogEventState
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.AbsentLiveData
import com.mi.mvi.utils.Constants.Companion.BLOG_FILTER
import com.mi.mvi.utils.Constants.Companion.BLOG_FILTER_DATE_UPDATED
import com.mi.mvi.utils.Constants.Companion.BLOG_ORDER
import com.mi.mvi.utils.Constants.Companion.BLOG_ORDER_ASC
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@ExperimentalCoroutinesApi
class BlogViewModel(
    private val searchBlogUseCase: SearchBlogUseCase,
    private val isAuthorBlogPostUseCase: IsAuthorBlogPostUseCase,
    private val deleteBlogPostUseCase: DeleteBlogPostUseCase,
    private val updateBlogPostUseCase: UpdateBlogPostUseCase,
    private val sessionManager: SessionManager,
    private val sheredPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BaseViewModel<BlogEventState, BlogViewState>() {

    init {
        setBlogFilter(
            sheredPreferences.getString(
                BLOG_FILTER,
                BLOG_FILTER_DATE_UPDATED
            )
        )

        setBlogOrder(
            sheredPreferences.getString(
                BLOG_ORDER,
                BLOG_ORDER_ASC
            )
        )
    }

    override fun handleEventState(eventState: BlogEventState): LiveData<DataState<BlogViewState>> {
        return when (eventState) {
            is BlogEventState.BlogSearchEvent -> {
                if (eventState.clearLayoutManagerState) {
                    clearLayoutManagerState()
                }
                sessionManager.cachedTokenEntity.value?.let { authToken ->
                    searchBlogUseCase.invoke(
                        tokenEntity = authToken,
                        query = getSearchQuery(),
                        filterAndOrder = getOrder() + getFilter(),
                        page = getPage()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.CheckAuthorBlogPostEvent -> {
                sessionManager.cachedTokenEntity.value?.let { authToken ->
                    isAuthorBlogPostUseCase.invoke(
                        tokenEntity = authToken,
                        slug = getSlug()
                    )
                } ?: AbsentLiveData.create()
            }
            is BlogEventState.DeleteBlogPostEvent -> {
                sessionManager.cachedTokenEntity.value?.let { authToken ->
                    deleteBlogPostUseCase.invoke(
                        tokenEntity = authToken,
                        blogPostEntity = getBlogPost()
                    )
                } ?: AbsentLiveData.create()
            }

            is BlogEventState.UpdateBlogPostEvent -> {

                return sessionManager.cachedTokenEntity.value?.let { authToken ->

                    val title = eventState.title
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val body = eventState.body
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    updateBlogPostUseCase.invoke(
                        authTokenEntity = authToken,
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
