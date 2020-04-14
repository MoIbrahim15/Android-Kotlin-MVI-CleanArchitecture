package com.mi.mvi.domain.repository

import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CreateBlogRepository {

    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): Flow<DataState<CreateBlogViewState>>
}