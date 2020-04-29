package com.mi.mvi.features.main.account

import com.mi.mvi.base.BaseFragment

import com.mi.mvi.mapper.UserMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseAccountFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val userMapper = UserMapper()
    val viewModel: AccountViewModel by sharedViewModel()
}
