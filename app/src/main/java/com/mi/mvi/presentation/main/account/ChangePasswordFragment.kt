package com.mi.mvi.presentation.main.account


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.mi.mvi.presentation.main.account.state.AccountEventState
import com.mi.mvi.presentation.main.account.state.AccountViewState
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class ChangePasswordFragment : BaseAccountFragment(R.layout.fragment_change_password) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        update_password_button.setOnClickListener {
            viewModel.setEventState(
                    AccountEventState.ChangePasswordEvent(
                            input_current_password.text.toString(),
                            input_new_password.text.toString(),
                            input_confirm_new_password.text.toString()
                    )
            )
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                dataStateChangeListener?.onDataStateChangeListener(dataState)
                dataState.data?.let {
                    it.response?.peekContent()?.let { content ->
                        if (content.messageRes == R.string.text_success) {
                            dataStateChangeListener?.hideSoftKeyboard()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        })
    }
}