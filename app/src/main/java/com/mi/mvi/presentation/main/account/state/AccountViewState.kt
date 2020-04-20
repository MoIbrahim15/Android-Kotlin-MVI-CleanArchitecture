package com.mi.mvi.presentation.main.account.state

import android.os.Parcelable
import com.mi.mvi.cache.entity.UserEntity
import kotlinx.android.parcel.Parcelize

const val ACCOUNT_VIEW_STATE_BUNDLE_KEY = "ACCOUNT_VIEW_STATE_BUNDLE_KEY"

@Parcelize
class AccountViewState(
    var userEntity: UserEntity? = null
) : Parcelable
