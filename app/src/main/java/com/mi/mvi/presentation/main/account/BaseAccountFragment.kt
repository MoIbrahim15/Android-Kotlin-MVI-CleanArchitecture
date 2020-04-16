package com.mi.mvi.presentation.main.account

import android.os.Bundle
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.mi.mvi.presentation.main.account.state.AccountViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
abstract class BaseAccountFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val viewModel: AccountViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { inState ->
            (inState[ACCOUNT_VIEW_STATE_BUNDLE_KEY] as AccountViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            ACCOUNT_VIEW_STATE_BUNDLE_KEY,
            viewModel.viewState.value
        )
        super.onSaveInstanceState(outState)
    }
}