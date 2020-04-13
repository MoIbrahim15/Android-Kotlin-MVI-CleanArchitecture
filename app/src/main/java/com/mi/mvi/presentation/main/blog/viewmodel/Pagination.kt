package com.mi.mvi.presentation.main.blog.viewmodel

import com.mi.mvi.presentation.main.blog.state.BlogEventState.BlogSearchEvent
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogsFields.page = 1
    setViewState(update)
}


@ExperimentalCoroutinesApi
fun BlogViewModel.loadFirstPage() {
    setQueryExhausted(false)
    resetPage()
    setEventState(BlogSearchEvent())
}

@ExperimentalCoroutinesApi
fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogsFields.page
    update.blogsFields.page = page + 1
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
    setQueryExhausted(viewState.blogsFields.isQueryExhausted)
    setBlogList(viewState.blogsFields.blogList)
}