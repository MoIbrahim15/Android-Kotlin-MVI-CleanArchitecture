package com.mi.mvi.remote.source

import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.remote.entity.BaseResponse
import com.mi.mvi.remote.entity.BlogListResponse
import com.mi.mvi.remote.entity.BlogPostResponse
import com.mi.mvi.remote.service.BlogAPIService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BlogRemoteDataSourceImpl(
    private val blogAPIService: BlogAPIService
) : BlogRemoteDataSource {
    override suspend fun searchListBlogPosts(
        authorization: String,
        query: String,
        ordering: String,
        page: Int
    ): BlogListResponse {
        return blogAPIService.searchListBlogPosts(authorization, query, ordering, page)
    }

    override suspend fun isAuthorOfBlogPost(authorization: String, slug: String): BaseResponse {
        return blogAPIService.isAuthorOfBlogPost(authorization, slug)
    }

    override suspend fun deleteBlogPost(authorization: String, slug: String): BaseResponse {
        return blogAPIService.deleteBlogPost(authorization, slug)
    }

    override suspend fun updateBlog(
        authorization: String,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostResponse {
        return blogAPIService.updateBlog(authorization, slug, title, body, image)
    }

    override suspend fun createBlog(
        authorization: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostResponse {
        return blogAPIService.createBlog(authorization, title, body, image)
    }
}