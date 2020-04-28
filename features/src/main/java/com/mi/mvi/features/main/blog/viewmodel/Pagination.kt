package com.mi.mvi.features.main.blog.viewmodel

import com.mi.mvi.domain.viewstate.BlogViewState
import com.mi.mvi.events.BlogEventState.BlogSearchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.refreshFromCache() {
    setQueryExhausted(false)
    setEventState(BlogSearchEvent(false))
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.loadFirstPage() {
    setQueryExhausted(false)
    resetPage()
    setEventState(BlogSearchEvent())
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page ?: 1
    update.blogFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.nextPage() {
    if (!getIsQueryExhausted()) {
        incrementPageNumber()
        setEventState(BlogSearchEvent())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState) {
    viewState.blogFields.let { blogFields ->
        blogFields.blogList?.let {
            setBlogListData(it.map { blogPostMapper.mapToView(it) }.toMutableList())
        }
    }
}
