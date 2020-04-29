package com.mi.mvi.events

import okhttp3.MultipartBody

sealed class BlogEventState {

    object BlogSearchEvent : BlogEventState()

    object CheckAuthorBlogPostEvent : BlogEventState()

    object DeleteBlogPostEvent : BlogEventState()

    data class UpdateBlogPostEvent(
        val title: String,
        val body: String,
        val image: MultipartBody.Part?
    ) : BlogEventState()

    object None : BlogEventState()
}
