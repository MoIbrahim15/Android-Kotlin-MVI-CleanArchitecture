package com.mi.mvi.presentation.main.blog.viewmodel

import com.mi.mvi.presentation.main.blog.state.BlogEventState.BlogSearchEvent
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.refreshFromCache() {
    setQueryExhausted(false)
    setEventState(BlogSearchEvent(false))
}

@ExperimentalCoroutinesApi
fun BlogViewModel.loadFirstPage() {
    setQueryExhausted(false)
    resetPage()
    setEventState(BlogSearchEvent())
}

@ExperimentalCoroutinesApi
private fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page ?: 1
    update.blogFields.page = page.plus(1)
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.nextPage() {
    if (!getIsQueryExhausted()) {
        incrementPageNumber()
        setEventState(BlogSearchEvent())
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState) {
    viewState.blogFields.let { blogFields ->
        blogFields.blogListEntity?.let { setBlogListData(it) }
    }
}
