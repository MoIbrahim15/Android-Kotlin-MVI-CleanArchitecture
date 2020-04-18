package com.mi.mvi.domain.usecase.blogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class DeleteBlogPostUseCase(private val repository: BlogRepository) {

    fun invoke(
        tokenEntity: AuthTokenEntity,
        blogPostEntity: BlogPostEntity
    ): LiveData<DataState<BlogViewState>> {
        return repository.deleteBlogPost(tokenEntity, blogPostEntity)
            .flowOn(Dispatchers.IO)
            .asLiveData()
    }
}