package com.mi.mvi.presentation.main.create_blog.state

import okhttp3.MultipartBody

sealed class CreateBlogEventState {

    data class CreateNewBlogEvent(
        val title: String,
        val body: String,
        val image: MultipartBody.Part
    ) : CreateBlogEventState()

    object None : CreateBlogEventState()
}