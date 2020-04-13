package com.mi.mvi.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlogListSearchResponse(
    @SerializedName("results")
    @Expose
    var results: MutableList<BlogSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
)