package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.datasource.model.*
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.presentation.main.blog.state.BlogFields
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.presentation.main.blog.state.ViewBlogFields
import com.mi.mvi.utils.DateUtils
import com.mi.mvi.utils.ErrorHandling
import com.mi.mvi.utils.SuccessHandling.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import com.mi.mvi.utils.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.MessageType
import com.mi.mvi.utils.response_handler.StateMessage
import com.mi.mvi.utils.response_handler.UIComponentType
import com.mi.mvi.utils.returnOrderedBlogQuery
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class BlogRepositoryImpl(
    private val blogRemoteDataSource: BlogRemoteDataSource,
    private val blogCacheDataSource: BlogCacheDataSource
) : BlogRepository {

    override fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogListSearchResponse, MutableList<BlogPost>, BlogViewState>(
                IO,
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
                }) {

            override suspend fun handleCacheSuccess(response: MutableList<BlogPost>?): DataState<BlogViewState>? {
                response?.let { items ->
                    return DataState.SUCCESS(
                        BlogViewState(
                            BlogFields(
                                items
                            )
                        )
                    )
                } ?: return buildDialogError(ErrorHandling.UNKNOWN_ERROR)
            }

            override suspend fun updateCache(networkObject: BlogListSearchResponse) {
                val cachedItems: MutableList<BlogPost> = mutableListOf()
                for (blogPostResponse in networkObject.results) {
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
            }

            override suspend fun handleNetworkSuccess(response: BlogListSearchResponse): DataState<BlogViewState>? {
                val cacheResponse = cacheCall?.invoke()
                return handleCacheSuccess(cacheResponse)
            }

        }.result
    }

    override fun isAuthorOfBlogPosts(
        authToken: AuthToken,
        slug: String
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.isAuthorOfBlogPost(
                        authorization = "Token ${authToken.token}",
                        slug = slug
                    )
                }) {
            override suspend fun handleNetworkSuccess(response: BaseResponse): DataState<BlogViewState>? {
                response.let { response ->
                    val isAuthor = response.response == RESPONSE_HAS_PERMISSION_TO_EDIT
                    return DataState.SUCCESS(
                        BlogViewState(
                            viewBlogFields = ViewBlogFields(
                                isAuthor = isAuthor
                            )
                        )
                    )
                }
            }
        }.result
    }

    override fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.deleteBlogPost(
                        authorization = "Token ${authToken.token}",
                        slug = blogPost.slug
                    )
                }) {
            override suspend fun updateCache(networkObject: BaseResponse) {
                val isDeleted = networkObject.response == SUCCESS_BLOG_DELETED
                if (isDeleted)
                    blogCacheDataSource.deleteBlogPost(blogPost)
            }

            override suspend fun handleNetworkSuccess(response: BaseResponse): DataState<BlogViewState>? {
                val isDeleted = response.response == SUCCESS_BLOG_DELETED
                if (isDeleted) {
                    return DataState.ERROR(
                        StateMessage(
                            message = "SUCCESS",
                            uiComponentType = UIComponentType.TOAST,
                            messageType = MessageType.SUCCESS
                        )
                    )
                } else {
                    return DataState.ERROR(
                        StateMessage(
                            message = "ERROR",
                            uiComponentType = UIComponentType.TOAST,
                            messageType = MessageType.ERROR
                        )
                    )
                }
            }

        }.result
    }

    override fun updateBlogPost(
        authToken: AuthToken,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogCreateUpdateResponse, BaseResponse, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.updateBlog(
                        "Token ${authToken.token!!}",
                        slug,
                        title,
                        body,
                        image
                    )
                }) {

            override suspend fun updateCache(networkObject: BlogCreateUpdateResponse) {
                val updatedBlogPost = BlogPost(
                    networkObject.pk,
                    networkObject.title,
                    networkObject.slug,
                    networkObject.body,
                    networkObject.image,
                    DateUtils.convertServerStringDateToLong(networkObject.date_updated),
                    networkObject.username
                )
                updatedBlogPost.let { blogPost ->
                    blogCacheDataSource.updateBlogPost(
                        blogPost.pk,
                        blogPost.title,
                        blogPost.body,
                        blogPost.image
                    )
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogCreateUpdateResponse): DataState<BlogViewState>? {
                val updatedBlogPost = BlogPost(
                    response.pk,
                    response.title,
                    response.slug,
                    response.body,
                    response.image,
                    DateUtils.convertServerStringDateToLong(response.date_updated),
                    response.username
                )
                return DataState.SUCCESS(
                    BlogViewState(
                        viewBlogFields = ViewBlogFields(
                            blogPost = updatedBlogPost
                        )
                    )
                )
            }

        }.result
    }
}