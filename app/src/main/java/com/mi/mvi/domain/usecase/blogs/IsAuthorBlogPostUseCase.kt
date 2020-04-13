package com.mi.mvi.domain.usecase.blogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn


@ExperimentalCoroutinesApi
class IsAuthorBlogPostUseCase(private val repository: BlogRepository) {

    fun invoke(
        token: AuthToken,
        slug: String
    ): LiveData<DataState<BlogViewState>> {
        return repository.isAuthorOfBlogPosts(token, slug)
            .flowOn(Dispatchers.IO)
            .asLiveData()
    }
}