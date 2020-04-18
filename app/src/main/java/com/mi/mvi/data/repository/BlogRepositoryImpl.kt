package com.mi.mvi.data.repository

import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.cache.returnOrderedBlogQuery
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.presentation.main.blog.state.BlogFields
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.presentation.main.blog.state.ViewBlogFields
import com.mi.mvi.remote.entity.BaseResponse
import com.mi.mvi.remote.entity.BlogListResponse
import com.mi.mvi.remote.entity.BlogPostResponse
import com.mi.mvi.utils.Constants.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import com.mi.mvi.utils.Constants.Companion.SUCCESS_BLOG_DELETED
import com.mi.mvi.utils.Constants.Companion.UNKNOWN_ERROR
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.MessageType
import com.mi.mvi.utils.response_handler.StateMessage
import com.mi.mvi.utils.response_handler.UIComponentType
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
        authTokenEntity: AuthTokenEntity,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogListResponse, MutableList<BlogPostEntity>, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.searchListBlogPosts(
                        authorization = "Token ${authTokenEntity.token}",
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

            override suspend fun handleCacheSuccess(response: MutableList<BlogPostEntity>?): DataState<BlogViewState>? {
                response?.let { items ->
                    return DataState.SUCCESS(
                        BlogViewState(
                            BlogFields(
                                items
                            )
                        )
                    )
                } ?: return buildDialogError(UNKNOWN_ERROR)
            }

            override suspend fun updateCache(networkObject: BlogListResponse) {
                val cachedItems: MutableList<BlogPostEntity> = mutableListOf()
                for (blogPostResponse in networkObject.results) {
                    cachedItems.add(
                        BlogPostEntity(
                            pk = blogPostResponse.pk,
                            title = blogPostResponse.title,
                            slug = blogPostResponse.slug,
                            image = blogPostResponse.image,
                            body = blogPostResponse.body,
                            date_updated = blogPostResponse.getDateAsLong(),
                            username = blogPostResponse.username
                        )
                    )
                }
                for (blogPost in cachedItems) {
                    blogCacheDataSource.insert(blogPost)
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogListResponse): DataState<BlogViewState>? {
                val cacheResponse = cacheCall?.invoke()
                return handleCacheSuccess(cacheResponse)
            }

        }.result
    }

    override fun isAuthorOfBlogPosts(
        authTokenEntity: AuthTokenEntity,
        slug: String
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.isAuthorOfBlogPost(
                        authorization = "Token ${authTokenEntity.token}",
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
        authTokenEntity: AuthTokenEntity,
        blogPostEntity: BlogPostEntity
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BaseResponse, BaseResponse, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.deleteBlogPost(
                        authorization = "Token ${authTokenEntity.token}",
                        slug = blogPostEntity.slug
                    )
                }) {
            override suspend fun updateCache(networkObject: BaseResponse) {
                val isDeleted = networkObject.response == SUCCESS_BLOG_DELETED
                if (isDeleted)
                    blogCacheDataSource.deleteBlogPost(blogPostEntity)
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
        authTokenEntity: AuthTokenEntity,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogPostResponse, BaseResponse, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.updateBlog(
                        "Token ${authTokenEntity.token!!}",
                        slug,
                        title,
                        body,
                        image
                    )
                }) {

            override suspend fun updateCache(networkObject: BlogPostResponse) {
                val updatedBlogPost = BlogPostEntity(
                    networkObject.pk,
                    networkObject.title,
                    networkObject.slug,
                    networkObject.body,
                    networkObject.image,
                    networkObject.getDateAsLong(),
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

            override suspend fun handleNetworkSuccess(response: BlogPostResponse): DataState<BlogViewState>? {
                val updatedBlogPost = BlogPostEntity(
                    response.pk,
                    response.title,
                    response.slug,
                    response.body,
                    response.image,
                    response.getDateAsLong(),
                    response.username
                )
                return DataState.SUCCESS(
                    BlogViewState(
                        viewBlogFields = ViewBlogFields(
                            blogPostEntity = updatedBlogPost
                        )
                    )
                )
            }

        }.result
    }
}