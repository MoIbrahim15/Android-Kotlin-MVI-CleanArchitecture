package com.mi.mvi.features.main.account

import android.os.Bundle
import com.mi.mvi.features.base.BaseFragment

import com.mi.mvi.domain.viewstate.AccountViewState
import com.mi.mvi.mapper.UserMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
const val ACCOUNT_VIEW_STATE_BUNDLE_KEY = "ACCOUNT_VIEW_STATE_BUNDLE_KEY"

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseAccountFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val userMapper = UserMapper()
    val viewModel: AccountViewModel by sharedViewModel()
}
