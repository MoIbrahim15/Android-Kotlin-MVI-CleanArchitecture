package com.mi.mvi.datasource.remote.blog

import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.datasource.model.BaseResponse
import com.mi.mvi.datasource.model.BlogCreateUpdateResponse
import com.mi.mvi.datasource.model.BlogListSearchResponse
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
    ): BlogListSearchResponse {
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
    ): BlogCreateUpdateResponse {
        return blogAPIService.updateBlog(authorization, slug, title, body, image)
    }
}