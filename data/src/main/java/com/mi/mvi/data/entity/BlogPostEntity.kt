package com.mi.mvi.data.entity

import java.text.SimpleDateFormat
import java.util.*


data class BlogPostEntity(
    var pk: Int,
    var title: String? = null,
    var slug: String? = null,
    var body: String? = null,
    var image: String? = null,
    var date_updated: String? = null,
    var username: String? = null
) : BaseEntity(){
    // dates from server look like this: "2019-07-23T03:28:01.406944Z"
    fun getDateAsLong(): Long {

        date_updated?.let {
            val stringDate =
                it.removeRange(it.indexOf("T") until it.length)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                return sdf.parse(stringDate).time
            } catch (e: Exception) {
                throw Exception(e)
            }
        } ?: return 0L
    }
}