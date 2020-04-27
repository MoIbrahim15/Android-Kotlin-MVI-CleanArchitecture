package com.mi.mvi.data.entity

data class UserEntity(
    var pk: Int,
    var email: String? = null,
    var username: String? = null,
    var token: String? = null
) : BaseEntity()
