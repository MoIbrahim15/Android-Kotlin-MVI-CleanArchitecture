package com.mi.mvi.data.repository.main

import com.mi.mvi.R
import com.mi.mvi.data.database.BlogPostDao
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.models.BlogPost
import com.mi.mvi.data.network.main.MainApiService
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.network.responses.BlogListSearchResponse
import com.mi.mvi.data.repository.BaseRepository
import com.mi.mvi.data.repository.NetworkBoundResource
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.ErrorConstants.Companion.RESPONSE_PERMISSION_TO_EDIT
import com.mi.mvi.data.response_handler.ErrorConstants.Companion.SUCCESS_BLOG_DELETED
import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.data.response_handler.Response
import com.mi.mvi.data.response_handler.ResponseView
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.ui.main.blog.state.BlogFields
import com.mi.mvi.ui.main.blog.state.BlogViewState
import com.mi.mvi.ui.main.blog.state.ViewBlogFields
import com.mi.mvi.utils.DateUtils
import com.mi.mvi.utils.returnOrderedBlogQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class BlogRepository(
    val apiService: MainApiService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager,
    val errorHandler: ErrorHandler
) : BaseRepository() {

    fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BlogListSearchResponse, MutableList<BlogPost>, BlogViewState>(
                    apiCall = {
                        apiService.searchListBlogPosts(
                            authorization = "Token ${authToken.token}",
                            query = query,
                            ordering = filterAndOrder,
                            page = page
                        )
                    },
                    cacheCall = {
                        blogPostDao.returnOrderedBlogQuery(
                            query = query,
                            filterAndOrder = filterAndOrder,
                            page = page
                        )
                    },
                    errorHandler = errorHandler,
                    canWorksOffline = true,
                    isNetworkAvailable = sessionManager.isConnectedToInternet()
                ) {
                override suspend fun handleNetworkSuccess(response: BlogListSearchResponse) {
                    val cachedItems: MutableList<BlogPost> = mutableListOf()
                    for (blogPostResponse in response.results) {
                        cachedItems.add(
                            BlogPost(
                                pk = blogPostResponse.pk,
                                title = blogPostResponse.title,
                                slug = blogPostResponse.slug,
                                image = blogPostResponse.image,
                                body = blogPostResponse.body,
                                date_updated = DateUtils.convertServerStringDateToLong(
                                    blogPostResponse.date_updated
                                ),
                                username = blogPostResponse.username
                            )
                        )
                    }
                    for (blogPost in cachedItems) {
                        blogPostDao.insert(blogPost)
                    }
                    val cacheResponse = cacheCall?.invoke()
                    handleCacheSuccess(cacheResponse)
                }

                override suspend fun handleCacheSuccess(response: MutableList<BlogPost>?) {
                    response?.let { items ->
                        emit(
                            DataState.SUCCESS(
                                BlogViewState(
                                    BlogFields(
                                        items
                                    )
                                )
                            )
                        )
                    }
                }

            }

        emitAll(networkBoundResource.call())
    }


    fun isAuthorOfBlogPosts(
        authToken: AuthToken,
        slug: String
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                    apiCall = {
                        apiService.isAuthorOfBlogPost(
                            authorization = "Token ${authToken.token}",
                            slug = slug
                        )
                    },
                    cacheCall = null,
                    errorHandler = errorHandler,
                    canWorksOffline = false,
                    isNetworkAvailable = sessionManager.isConnectedToInternet()
                ) {
                override suspend fun handleNetworkSuccess(response: BaseResponse) {
                    response?.let { response ->
                        val isAuthor = response.response == RESPONSE_PERMISSION_TO_EDIT
                        emit(
                            DataState.SUCCESS(
                                BlogViewState(
                                    viewBlogFields = ViewBlogFields(
                                        isAuthor = isAuthor
                                    )
                                )
                            )
                        )
                    }
                }

                override suspend fun handleCacheSuccess(response: BaseResponse?) {

                }

            }

        emitAll(networkBoundResource.call())
    }


    fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                    apiCall = {
                        apiService.deleteBlogPost(
                            authorization = "Token ${authToken.token}",
                            slug = blogPost.slug
                        )
                    },
                    cacheCall = null,
                    errorHandler = errorHandler,
                    canWorksOffline = false,
                    isNetworkAvailable = sessionManager.isConnectedToInternet()
                ) {
                override suspend fun handleNetworkSuccess(response: BaseResponse) {
                    response?.let { response ->
                        val isDeleted = response.response == SUCCESS_BLOG_DELETED
                        if (isDeleted) {
                            blogPostDao.deleteBlogPost(blogPost)
                            emit(
                                DataState.SUCCESS(
                                    data = null, response = Response(
                                        messageRes = R.string.text_success,
                                        responseView = ResponseView.TOAST()
                                    )
                                )
                            )
                        } else {
                            emit(
                                DataState.ERROR(
                                    response =
                                    Response(
                                        messageRes = R.string.deleted,
                                        responseView = ResponseView.TOAST()
                                    )
                                )
                            )
                        }
                    }
                }

                override suspend fun handleCacheSuccess(response: BaseResponse?) {

                }

            }

        emitAll(networkBoundResource.call())
    }
}