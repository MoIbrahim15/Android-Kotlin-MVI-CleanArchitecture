package com.mi.mvi.domain.repository

import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BlogRepository {

    fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>>

    fun isAuthorOfBlogPosts(
        authToken: AuthToken,
        slug: String
    ): Flow<DataState<BlogViewState>>


    fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): Flow<DataState<BlogViewState>>


    fun updateBlogPost(
        authToken: AuthToken,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>>
}