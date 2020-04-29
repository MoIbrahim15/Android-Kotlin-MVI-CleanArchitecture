package com.mi.mvi.domain.repository

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.BlogPost
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.viewstate.BlogViewState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BlogRepository : BaseRepository {

    fun searchBlogPosts(
        token: Token,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>>

    fun isAuthorOfBlogPosts(
        token: Token,
        slug: String
    ): Flow<DataState<BlogViewState>>

    fun deleteBlogPost(
        token: Token,
        blogPost: BlogPost
    ): Flow<DataState<BlogViewState>>

    fun updateBlogPost(
        token: Token,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>>

    fun saveFilterOptions(filter: String, order: String)

    fun getFilter(): String?

    fun getOrder(): String?
}
