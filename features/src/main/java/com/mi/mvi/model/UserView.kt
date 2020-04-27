package com.mi.mvi.model


data class UserView(
    var pk: Int,
    var email: String?,
    var username: String?,
    var token: String?
) : BaseModelView()
