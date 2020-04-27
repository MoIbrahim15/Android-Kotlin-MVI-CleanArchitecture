package com.mi.mvi.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class CachedUser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "email")
    var email: String?=null,

    @ColumnInfo(name = "username")
    var username: String?=null
)
