package com.mi.mvi.domain.model

data class User(
    var pk: Int,
    var email: String?,
    var username: String?,
    var token: String?
) : BaseModel()
