package com.mi.mvi.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class RemoteBlogPost(
    @SerializedName("response") @Expose var response: String? = null,
    @SerializedName("pk") @Expose var pk: Int,
    @SerializedName("title") @Expose var title: String? = null,
    @SerializedName("slug") @Expose var slug: String? = null,
    @SerializedName("body") @Expose var body: String? = null,
    @SerializedName("image") @Expose var image: String? = null,
    @SerializedName("date_updated") @Expose var date_updated: String? = null,
    @SerializedName("username") @Expose var username: String? = null
) {

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
