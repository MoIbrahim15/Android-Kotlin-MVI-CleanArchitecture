package com.mi.mvi.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "blog_post")
data class CachedBlogPost(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "slug")
    var slug: String? = null,

    @ColumnInfo(name = "body")
    var body: String? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "date_updated")
    var date_updated: Long? = null,

    @ColumnInfo(name = "username")
    var username: String? = null
) {

    fun getDateAsString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            return sdf.format(Date(date_updated!!))
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

}
