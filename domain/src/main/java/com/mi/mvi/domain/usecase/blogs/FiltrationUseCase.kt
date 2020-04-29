package com.mi.mvi.domain.usecase.blogs

import com.mi.mvi.domain.repository.BlogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FiltrationUseCase(private val repository: BlogRepository) {

    fun saveFilterOptions(filter: String, order: String) {
        repository.saveFilterOptions(filter, order)
    }

    fun getFilter(): String? {
        return repository.getFilter()
    }

    fun getOrder(): String? {
        return repository.getOrder()
    }
}