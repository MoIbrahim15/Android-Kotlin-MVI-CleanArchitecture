package com.mi.mvi.data.repository

import com.mi.mvi.R
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.datasource.model.*
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.presentation.main.blog.state.BlogFields
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.presentation.main.blog.state.ViewBlogFields
import com.mi.mvi.utils.DateUtils
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.RESPONSE_PERMISSION_TO_EDIT
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.SUCCESS_BLOG_DELETED
import com.mi.mvi.utils.response_handler.ErrorHandler
import com.mi.mvi.utils.response_handler.Response
import com.mi.mvi.utils.response_handler.ResponseView
import com.mi.mvi.utils.returnOrderedBlogQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class BlogRepositoryImpl(
    private val blogRemoteDataSource: BlogRemoteDataSource,
    private val blogCacheDataSource: BlogCacheDataSource,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler
) : BlogRepository {

    override fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BlogListSearchResponse, MutableList<BlogPost>, BlogViewState>(
                    apiCall = {
                        blogRemoteDataSource.searchListBlogPosts(
                            authorization = "Token ${authToken.token}",
                            query = query,
                            ordering = filterAndOrder,
                            page = page
                        )
                    },
                    cacheCall = {
                        blogCacheDataSource.returnOrderedBlogQuery(
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
                        blogCacheDataSource.insert(blogPost)
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


    override fun isAuthorOfBlogPosts(
        authToken: AuthToken,
        slug: String
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                    apiCall = {
                        blogRemoteDataSource.isAuthorOfBlogPost(
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


    override fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                    apiCall = {
                        blogRemoteDataSource.deleteBlogPost(
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
                            blogCacheDataSource.deleteBlogPost(blogPost)
                            emit(
                                DataState.SUCCESS<BlogViewState>(
                                    data = null, response = Response(
                                        messageRes = R.string.text_success,
                                        responseView = ResponseView.TOAST()
                                    )
                                )
                            )
                        } else {
                            emit(
                                DataState.ERROR<BlogViewState>(
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

    override fun updateBlogPost(
        authToken: AuthToken,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BlogCreateUpdateResponse, BaseResponse, BlogViewState>(
                    apiCall = {
                        blogRemoteDataSource.updateBlog(
                            "Token ${authToken.token!!}",
                            slug,
                            title,
                            body,
                            image
                        )
                    },
                    cacheCall = null,
                    errorHandler = errorHandler,
                    canWorksOffline = false,
                    isNetworkAvailable = sessionManager.isConnectedToInternet()
                ) {
                override suspend fun handleNetworkSuccess(response: BlogCreateUpdateResponse) {

                    val updatedBlogPost = BlogPost(
                        response.pk,
                        response.title,
                        response.slug,
                        response.body,
                        response.image,
                        DateUtils.convertServerStringDateToLong(response.date_updated),
                        response.username
                    )
                    updatedBlogPost.let { blogPost ->
                        blogCacheDataSource.updateBlogPost(
                            blogPost.pk,
                            blogPost.title,
                            blogPost.body,
                            blogPost.image
                        )
                    }

                    DataState.SUCCESS(
                        BlogViewState(
                            viewBlogFields = ViewBlogFields(
                                blogPost = updatedBlogPost
                            )
                        ),
                        Response(R.string.text_success, ResponseView.TOAST())
                    )
                }

                override suspend fun handleCacheSuccess(response: BaseResponse?) {

                }

            }

        emitAll(networkBoundResource.call())
    }
}