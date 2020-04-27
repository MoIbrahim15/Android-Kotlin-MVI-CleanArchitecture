package com.mi.mvi.remote.source

import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.BlogPostEntity
import com.mi.mvi.data.entity.BlogPostListEntity
import com.mi.mvi.remote.mapper.BaseEntityMapper
import com.mi.mvi.remote.mapper.BlogPostEntityMapper
import com.mi.mvi.remote.mapper.BlogPostListEntityMapper
import com.mi.mvi.remote.service.BlogAPIService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BlogRemoteDataSourceImpl(
    private val blogAPIService: BlogAPIService,
    private val baseEntityMapper: BaseEntityMapper,
    private val blogPostEntityMapper: BlogPostEntityMapper,
    private val blogPostListEntityMapper: BlogPostListEntityMapper

) : BlogRemoteDataSource {

    override suspend fun searchListBlogPosts(
        authorization: String,
        query: String,
        ordering: String,
        page: Int
    ): BlogPostListEntity {
        return blogPostListEntityMapper.mapFromRemote(
            blogAPIService.searchListBlogPosts(
                authorization,
                query,
                ordering,
                page
            )
        )
    }

    override suspend fun isAuthorOfBlogPost(authorization: String, slug: String): BaseEntity {
        return baseEntityMapper.mapFromRemote(
            blogAPIService.isAuthorOfBlogPost(
                authorization,
                slug
            )
        )
    }

    override suspend fun deleteBlogPost(authorization: String, slug: String?): BaseEntity {
        return baseEntityMapper.mapFromRemote(blogAPIService.deleteBlogPost(authorization, slug))
    }

    override suspend fun updateBlog(
        authorization: String,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostEntity {
        return blogPostEntityMapper.mapFromRemote(
            blogAPIService.updateBlog(
                authorization,
                slug,
                title,
                body,
                image
            )
        )
    }

    override suspend fun createBlog(
        authorization: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostEntity {
        return blogPostEntityMapper.mapFromRemote(
            blogAPIService.createBlog(
                authorization,
                title,
                body,
                image
            )
        )
    }
}
