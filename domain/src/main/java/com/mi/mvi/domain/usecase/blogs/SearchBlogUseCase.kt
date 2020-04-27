package com.mi.mvi.domain.usecase.blogs


import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.domain.viewstate.BlogViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class SearchBlogUseCase(private val repository: BlogRepository) {

    fun invoke(
        token: Token,
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<DataState<BlogViewState>> {
        return repository.searchBlogPosts(token, query, filterAndOrder, page)
                .flowOn(Dispatchers.IO)
    }
}
