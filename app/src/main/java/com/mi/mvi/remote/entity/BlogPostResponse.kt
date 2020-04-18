package com.mi.mvi.remote.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class BlogPostResponse (
    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("slug")
    @Expose
    var slug: String,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("image")
    @Expose
    var image: String,

    @SerializedName("date_updated")
    @Expose
    var date_updated: String,

    @SerializedName("username")
    @Expose
    var username: String
){

    // dates from server look like this: "2019-07-23T03:28:01.406944Z"
    fun getDateAsLong(): Long{
        val stringDate = date_updated.removeRange(date_updated.indexOf("T") until date_updated.length)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            return sdf.parse(stringDate).time
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}