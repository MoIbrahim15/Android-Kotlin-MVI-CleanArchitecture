package com.mi.mvi.data.datasource.remote

import com.mi.mvi.remote.entity.BaseResponse
import com.mi.mvi.remote.entity.BlogListResponse
import com.mi.mvi.remote.entity.BlogPostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BlogRemoteDataSource {

    suspend fun searchListBlogPosts(
        authorization: String,
       query: String,
      ordering: String,
        page: Int
    ): BlogListResponse


    suspend fun isAuthorOfBlogPost(
      authorization: String,
       slug: String
    ): BaseResponse


    suspend fun deleteBlogPost(
       authorization: String,
       slug: String
    ): BaseResponse


    suspend fun updateBlog(
       authorization: String,
       slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostResponse


    suspend fun createBlog(
        authorization: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogPostResponse
}