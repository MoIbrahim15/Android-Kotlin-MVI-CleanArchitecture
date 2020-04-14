package com.mi.mvi.domain.usecase.blogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody


@ExperimentalCoroutinesApi
class CreateBlogUseCase(private val repository: CreateBlogRepository) {
    fun invoke(
        token: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): LiveData<DataState<CreateBlogViewState>> {
        return repository.createNewBlogPost(
            token,
            title,
            body,
            image
        ).flowOn(Dispatchers.IO)
            .asLiveData()
    }
}