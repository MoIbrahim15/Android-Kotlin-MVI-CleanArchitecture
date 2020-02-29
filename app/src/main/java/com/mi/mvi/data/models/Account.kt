package com.mi.mvi.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk : Int,

    @ColumnInfo(name = "email")
    var email : String,

    @ColumnInfo(name = "username")
    var username : String
)