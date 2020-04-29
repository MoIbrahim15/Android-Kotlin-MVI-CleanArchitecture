package com.mi.mvi.domain.usecase.blogs

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.domain.viewstate.CreateBlogViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class CreateBlogUseCase(private val repository: CreateBlogRepository) {
    fun invoke(
        tokenEntity: Token,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): Flow<DataState<CreateBlogViewState>> {
        return repository.createNewBlogPost(
            tokenEntity,
            title,
            body,
            image
        ).flowOn(Dispatchers.IO)
    }
}
