package com.mi.mvi.domain.repository

import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BlogRepository {

    fun searchBlogPosts(
        authTokenEntity: AuthTokenEntity,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>>

    fun isAuthorOfBlogPosts(
        authTokenEntity: AuthTokenEntity,
        slug: String
    ): Flow<DataState<BlogViewState>>

    fun deleteBlogPost(
        authTokenEntity: AuthTokenEntity,
        blogPostEntity: BlogPostEntity
    ): Flow<DataState<BlogViewState>>

    fun updateBlogPost(
        authTokenEntity: AuthTokenEntity,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>>
}
