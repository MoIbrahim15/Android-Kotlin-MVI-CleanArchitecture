package com.mi.mvi.features.main.account

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.domain.Constants.Companion.SUCCESS
import com.mi.mvi.eventstate.AccountEventState
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
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
                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.message == SUCCESS) {
                        uiCommunicationListener?.hideSoftKeyboard()
                        findNavController().popBackStack()
                    }
                }
            }
        })
    }
}
