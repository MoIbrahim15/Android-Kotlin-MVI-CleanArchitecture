package com.mi.mvi.cache.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

const val AUTH_TOKEN_BUNDLE_KEY = "AUTH_TOKEN_BUNDLE_KEY"

@Parcelize
@Entity(
    tableName = "auth_token",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["pk"],
        childColumns = ["account_pk"],
        onDelete = CASCADE
    )]
)
data class AuthTokenEntity(

    @PrimaryKey
    @ColumnInfo(name = "account_pk")
    var account_pk: Int? = -1,

    @ColumnInfo(name = "token")
    var token: String? = null
) : Parcelable
