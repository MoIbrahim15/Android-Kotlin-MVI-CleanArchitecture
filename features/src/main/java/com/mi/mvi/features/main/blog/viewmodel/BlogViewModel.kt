package com.mi.mvi.features.main.blog.viewmodel

import com.mi.mvi.base.BaseViewModel
import com.mi.mvi.common.SessionManager
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.usecase.blogs.*
import com.mi.mvi.domain.viewstate.BlogViewState
import com.mi.mvi.events.BlogEventState
import com.mi.mvi.mapper.BlogPostMapper
import com.mi.mvi.mapper.TokenMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
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
    private val filtrationUseCase: FiltrationUseCase,
    private val sessionManager: SessionManager,
    private val tokenMapper: TokenMapper,
    val blogPostMapper: BlogPostMapper
) : BaseViewModel<BlogEventState, BlogViewState>() {

    init {
        setBlogFilter(filtrationUseCase.getFilter())
        setBlogOrder(filtrationUseCase.getOrder())
    }

    override fun handleEventState(eventState: BlogEventState): Flow<DataState<BlogViewState>> =
        flow {
            when (eventState) {
                is BlogEventState.BlogSearchEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        emitAll(
                            searchBlogUseCase.invoke(
                                token = tokenMapper.mapFromView(authToken),
                                query = getSearchQuery(),
                                filterAndOrder = getOrder() + getFilter(),
                                page = getPage()
                            )
                        )
                    }
                }
                is BlogEventState.CheckAuthorBlogPostEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        emitAll(
                            isAuthorBlogPostUseCase.invoke(
                                token = tokenMapper.mapFromView(authToken),
                                slug = getSlug()
                            )
                        )
                    }
                }
                is BlogEventState.DeleteBlogPostEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        emitAll(
                            deleteBlogPostUseCase.invoke(
                                token = tokenMapper.mapFromView(authToken),
                                blogPost = getBlogPost()
                            )
                        )
                    }
                }

                is BlogEventState.UpdateBlogPostEvent -> {

                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->

                        val title = eventState.title
                            .toRequestBody("text/plain".toMediaTypeOrNull())
                        val body = eventState.body
                            .toRequestBody("text/plain".toMediaTypeOrNull())

                        emitAll(
                            updateBlogPostUseCase.invoke(
                                token = tokenMapper.mapFromView(authToken),
                                slug = getSlug(),
                                title = title,
                                body = body,
                                image = eventState.image
                            )
                        )
                    }
                }
            }
        }

    fun saveFilterOptions(filter: String, order: String) {
        filtrationUseCase.saveFilterOptions(filter, order)
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }
}
