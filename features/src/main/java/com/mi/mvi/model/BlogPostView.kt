package com.mi.mvi.domain.model

import com.mi.mvi.model.BaseModelView


data class BlogPostView(
    var pk: Int,
    var title: String? = null,
    var slug: String? = null,
    var body: String? = null,
    var image: String? = null,
    var date_updated: String? = null,
    var username: String? = null
) : BaseModelView()