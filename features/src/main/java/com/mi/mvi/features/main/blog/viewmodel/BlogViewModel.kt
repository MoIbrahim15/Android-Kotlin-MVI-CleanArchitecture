package com.mi.mvi.features.main.blog.viewmodel

import android.content.SharedPreferences
import com.mi.mvi.domain.Constants.Companion.BLOG_FILTER_DATE_UPDATED
import com.mi.mvi.domain.Constants.Companion.BLOG_ORDER_ASC
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.usecase.blogs.DeleteBlogPostUseCase
import com.mi.mvi.domain.usecase.blogs.IsAuthorBlogPostUseCase
import com.mi.mvi.domain.usecase.blogs.SearchBlogUseCase
import com.mi.mvi.domain.usecase.blogs.UpdateBlogPostUseCase
import com.mi.mvi.domain.viewstate.BlogViewState
import com.mi.mvi.events.BlogEventState
import com.mi.mvi.features.base.BaseViewModel
import com.mi.mvi.features.common.SessionManager
import com.mi.mvi.mapper.BlogPostMapper
import com.mi.mvi.mapper.TokenMapper
import com.mi.mvi.utils.Constants.Companion.BLOG_FILTER
import com.mi.mvi.utils.Constants.Companion.BLOG_ORDER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@FlowPreview
@ExperimentalCoroutinesApi
class BlogViewModel(
    private val searchBlogUseCase: SearchBlogUseCase,
    private val isAuthorBlogPostUseCase: IsAuthorBlogPostUseCase,
    private val deleteBlogPostUseCase: DeleteBlogPostUseCase,
    private val updateBlogPostUseCase: UpdateBlogPostUseCase,
    private val sessionManager: SessionManager,
    private val sheredPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor,
    private val tokenMapper: TokenMapper,
    val blogPostMapper: BlogPostMapper
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

    override fun handleEventState(eventState: BlogEventState): Flow<DataState<BlogViewState>> =
        flow {
            when (eventState) {
                is BlogEventState.BlogSearchEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        searchBlogUseCase.invoke(
                            token = tokenMapper.mapFromView(authToken),
                            query = getSearchQuery(),
                            filterAndOrder = getOrder() + getFilter(),
                            page = getPage()
                        )
                    }
                }
                is BlogEventState.CheckAuthorBlogPostEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        isAuthorBlogPostUseCase.invoke(
                            token = tokenMapper.mapFromView(authToken),
                            slug = getSlug()
                        )
                    }
                }
                is BlogEventState.DeleteBlogPostEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        deleteBlogPostUseCase.invoke(
                            token = tokenMapper.mapFromView(authToken),
                            blogPost = getBlogPost()
                        )
                    }
                }

                is BlogEventState.UpdateBlogPostEvent -> {

                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->

                        val title = eventState.title
                            .toRequestBody("text/plain".toMediaTypeOrNull())
                        val body = eventState.body
                            .toRequestBody("text/plain".toMediaTypeOrNull())

                        updateBlogPostUseCase.invoke(
                            token = tokenMapper.mapFromView(authToken),
                            slug = getSlug(),
                            title = title,
                            body = body,
                            image = eventState.image
                        )
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
