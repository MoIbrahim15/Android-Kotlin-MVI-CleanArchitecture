package com.mi.mvi.ui.main.account


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.main.account.state.AccountEventState
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class ChangePasswordFragment : BaseFragment(R.layout.fragment_change_password) {

    private val accountViewModel: AccountViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        update_password_button.setOnClickListener {
            accountViewModel.setEventState(
                    AccountEventState.ChangePasswordEvent(
                            input_current_password.text.toString(),
                            input_new_password.text.toString(),
                            input_confirm_new_password.text.toString()
                    )
            )
        }
    }

    private fun subscribeObservers() {
        accountViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateChangeListener?.onDataStateChangeListener(dataState)

            dataState.data?.let {
                it.response?.peekContent()?.let { content ->
                    if (content.messageRes == R.string.text_success) {
                        dataStateChangeListener?.hideSoftKeyboard()
                        findNavController().popBackStack()
                    }
                }
            }
        })
    }
}