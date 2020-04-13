package com.mi.mvi.datasource.remote.blog

import com.mi.mvi.datasource.model.BaseResponse
import com.mi.mvi.datasource.model.BlogCreateUpdateResponse
import com.mi.mvi.datasource.model.BlogListSearchResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface BlogAPIService {

    @GET("blog/list")
    suspend fun searchListBlogPosts(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): BlogListSearchResponse


    @GET("blog/{slug}/is_author")
    suspend fun isAuthorOfBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): BaseResponse


    @DELETE("blog/{slug}/delete")
    suspend fun deleteBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): BaseResponse


    @Multipart
    @PUT("blog/{slug}/update")
    suspend fun updateBlog(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): BlogCreateUpdateResponse
}