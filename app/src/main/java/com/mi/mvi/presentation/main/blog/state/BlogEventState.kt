package com.mi.mvi.presentation.main.blog.state

import okhttp3.MultipartBody

sealed class BlogEventState {

    class BlogSearchEvent : BlogEventState()

    class CheckAuthorBlogPostEvent : BlogEventState()

    class DeleteBlogPostEvent: BlogEventState()

    data class UpdateBlogPostEvent(
        val title: String,
        val body: String,
        val image: MultipartBody.Part?
    ): BlogEventState()


    class None : BlogEventState()
}