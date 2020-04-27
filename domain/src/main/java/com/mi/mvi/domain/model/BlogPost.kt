package com.mi.mvi.domain.model


data class BlogPost(
    var pk: Int,
    var title: String? = null,
    var slug: String? = null,
    var body: String? = null,
    var image: String? = null,
    var date_updated: String? = null,
    var username: String? = null
) : BaseModel()