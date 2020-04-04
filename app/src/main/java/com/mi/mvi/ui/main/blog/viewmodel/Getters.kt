package com.mi.mvi.ui.main.blog.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun BlogViewModel.getSearchQuery() :  String{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.searchQuery
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getPage() :  Int{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.page
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getIsQueryExhausted() : Boolean{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.isQueryExhausted
    }
}
