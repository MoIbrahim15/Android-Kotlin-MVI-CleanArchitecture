package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.cache.returnOrderedBlogQuery
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.BlogPostEntity
import com.mi.mvi.data.entity.BlogPostListEntity
import com.mi.mvi.data.mapper.BlogPostMapper
import com.mi.mvi.domain.Constants.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import com.mi.mvi.domain.Constants.Companion.SUCCESS_BLOG_DELETED
import com.mi.mvi.domain.Constants.Companion.UNKNOWN_ERROR
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.MessageType
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.domain.model.BlogPost
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.domain.viewstate.BlogFields
import com.mi.mvi.domain.viewstate.BlogViewState
import com.mi.mvi.domain.viewstate.ViewBlogFields
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class BlogRepositoryImpl(
    private val blogRemoteDataSource: BlogRemoteDataSource,
    private val blogCacheDataSource: BlogCacheDataSource,
    private val blogPostMapper: BlogPostMapper
) : BlogRepository {

    override fun searchBlogPosts(
        token: Token,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogPostListEntity, MutableList<BlogPostEntity>, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.searchListBlogPosts(
                        authorization = "Token ${token.token}",
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
                                items.map { item ->
                                    blogPostMapper.mapFromEntity(item)
                                }.toMutableList()
                            )
                        )
                    )
                } ?: return buildDialogError(UNKNOWN_ERROR)
            }

            override suspend fun updateCache(networkObject: BlogPostListEntity) {
                val cachedItems: MutableList<BlogPostEntity> = mutableListOf()
                for (blogPostResponse in networkObject.results) {
                    cachedItems.add(
                        BlogPostEntity(
                            pk = blogPostResponse.pk,
                            title = blogPostResponse.title,
                            slug = blogPostResponse.slug,
                            image = blogPostResponse.image,
                            body = blogPostResponse.body,
                            date_updated = blogPostResponse.date_updated,
                            username = blogPostResponse.username
                        )
                    )
                }
                for (blogPost in cachedItems) {
                    blogCacheDataSource.insert(blogPost)
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogPostListEntity): DataState<BlogViewState>? {
                val cacheResponse = cacheCall?.invoke()
                return handleCacheSuccess(cacheResponse)
            }
        }.result
    }

    override fun isAuthorOfBlogPosts(
        token: Token,
        slug: String
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BaseEntity, BaseEntity, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.isAuthorOfBlogPost(
                        authorization = "Token ${token.token}",
                        slug = slug
                    )
                }) {
            override suspend fun handleNetworkSuccess(response: BaseEntity): DataState<BlogViewState>? {
                response.let { baseEntity ->
                    val isAuthor = baseEntity.response == RESPONSE_HAS_PERMISSION_TO_EDIT
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
        token: Token,
        blogPost: BlogPost
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BaseEntity, BaseEntity, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.deleteBlogPost(
                        authorization = "Token ${token.token}",
                        slug = blogPost.slug
                    )
                }) {
            override suspend fun updateCache(networkObject: BaseEntity) {
                val isDeleted = networkObject.response == SUCCESS_BLOG_DELETED
                if (isDeleted)
                    blogCacheDataSource.deleteBlogPost(blogPostMapper.mapToEntity(blogPost))
            }

            override suspend fun handleNetworkSuccess(response: BaseEntity): DataState<BlogViewState>? {
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
        token: Token,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogPostEntity, BaseEntity, BlogViewState>(
                IO,
                apiCall = {
                    blogRemoteDataSource.updateBlog(
                        "Token ${token.token}",
                        slug,
                        title,
                        body,
                        image
                    )
                }) {

            override suspend fun updateCache(networkObject: BlogPostEntity) {

                networkObject.let { blogPost ->
                    blogCacheDataSource.updateBlogPost(
                        blogPost.pk,
                        blogPost.title,
                        blogPost.body,
                        blogPost.image
                    )
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogPostEntity): DataState<BlogViewState>? {
                val updatedBlogPost = BlogPostEntity(
                    response.pk,
                    response.title,
                    response.slug,
                    response.body,
                    response.image,
                    response.date_updated,
                    response.username
                )
                return DataState.SUCCESS(
                    BlogViewState(
                        viewBlogFields = ViewBlogFields(
                            blogPostEntity = blogPostMapper.mapFromEntity(updatedBlogPost)
                        )
                    )
                )
            }
        }.result
    }

    override fun saveFilterOptions(filter: String, order: String) {
        blogCacheDataSource.saveFilterOptions(filter, order)
    }

    override fun getFilter(): String? {
       return blogCacheDataSource.getFilter()
    }

    override fun getOrder(): String? {
        return blogCacheDataSource.getOrder()
    }
}
