package com.mi.mvi.data.datasource.remote

import com.mi.mvi.datasource.model.BaseResponse
import com.mi.mvi.datasource.model.BlogCreateUpdateResponse
import com.mi.mvi.datasource.model.BlogListSearchResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BlogRemoteDataSource {

    suspend fun searchListBlogPosts(
        authorization: String,
       query: String,
      ordering: String,
        page: Int
    ): BlogListSearchResponse


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
    ): BlogCreateUpdateResponse


    suspend fun createBlog(
        authorization: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): BlogCreateUpdateResponse
}