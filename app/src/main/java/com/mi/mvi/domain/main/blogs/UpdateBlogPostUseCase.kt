package com.mi.mvi.domain.main.blogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.repository.main.BlogRepository
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.ui.main.blog.state.BlogViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class UpdateBlogPostUseCase(val repository: BlogRepository) {

    fun invoke(
        authToken: AuthToken,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): LiveData<DataState<BlogViewState>> {
        return repository.updateBlogPost(authToken, slug, title, body, image)
            .flowOn(Dispatchers.IO)
            .asLiveData()
    }
}