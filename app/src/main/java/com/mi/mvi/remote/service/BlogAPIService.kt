package com.mi.mvi.remote.service

import com.mi.mvi.remote.entity.BaseResponse
import com.mi.mvi.remote.entity.BlogListResponse
import com.mi.mvi.remote.entity.BlogPostResponse
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
    ): BlogListResponse


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
    ): BlogPostResponse

    @Multipart
    @POST("blog/create")
    suspend fun createBlog(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): BlogPostResponse
}