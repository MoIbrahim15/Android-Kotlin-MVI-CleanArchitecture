package com.mi.mvi.domain.usecase.blogs

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.domain.viewstate.BlogViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class UpdateBlogPostUseCase(private val repository: BlogRepository) {

    fun invoke(
        token: Token,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): Flow<DataState<BlogViewState>> {
        return repository.updateBlogPost(token, slug, title, body, image)
            .flowOn(Dispatchers.IO)
    }
}
