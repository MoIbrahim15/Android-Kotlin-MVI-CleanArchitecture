package com.mi.mvi.ui.main.blog.state

sealed class BlogEventState {

    class BlogSearchEvent : BlogEventState()

    class CheckAuthorBlogPostEvent : BlogEventState()

    class DeleteBlogPostEvent: BlogEventState()

    class None : BlogEventState()
}