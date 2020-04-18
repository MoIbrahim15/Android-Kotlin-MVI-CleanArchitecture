package com.mi.mvi.cache.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity(tableName = "blog_post")
data class BlogPostEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "slug")
    var slug: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "date_updated")
    var date_updated: Long,

    @ColumnInfo(name = "username")
    var username: String
) : Parcelable{

    fun getDateAsString(): String{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            val date = sdf.format(Date(date_updated))
            return date
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}