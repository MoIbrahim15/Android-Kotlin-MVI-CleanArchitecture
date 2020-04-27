package com.mi.mvi.data.datasource.remote

import com.mi.mvi.data.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BlogRemoteDataSource {

    suspend fun searchListBlogPosts(
        authorization: String,
        query: String,
        ordering: String,
        page: Int
    ): BlogPostListEntity

    suspend fun isAuthorOfBlogPost(
        authorization: String,
        slug: String
    ): BaseEntity

    suspend fun deleteBlogPost(
        authorization: String,
        slug: String?
    ): BaseEntity

    suspend fun updateBlog(
        authorization: String,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostEntity

    suspend fun createBlog(
        authorization: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostEntity
}
