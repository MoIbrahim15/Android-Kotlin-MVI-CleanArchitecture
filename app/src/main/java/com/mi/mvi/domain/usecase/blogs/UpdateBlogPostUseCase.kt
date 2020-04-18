package com.mi.mvi.domain.usecase.blogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class UpdateBlogPostUseCase(private val repository: BlogRepository) {

    fun invoke(
        authTokenEntity: AuthTokenEntity,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): LiveData<DataState<BlogViewState>> {
        return repository.updateBlogPost(authTokenEntity, slug, title, body, image)
            .flowOn(Dispatchers.IO)
            .asLiveData()
    }
}