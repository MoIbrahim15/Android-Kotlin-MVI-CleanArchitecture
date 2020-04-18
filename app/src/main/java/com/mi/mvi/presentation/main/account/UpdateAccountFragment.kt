package com.mi.mvi.presentation.main.account


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.presentation.main.account.state.AccountEventState
import kotlinx.android.synthetic.main.fragment_update_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UpdateAccountFragment : BaseAccountFragment(R.layout.fragment_update_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                dataStateChangeListener?.onDataStateChangeListener(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.userEntity?.let { accountProperties ->
                    setAccountProperties(userEntity = accountProperties)
                }
            }
        })
    }

    private fun setAccountProperties(userEntity: UserEntity) {
        input_email.setText(userEntity.email)
        input_username.setText(userEntity.username)
        input_email.setText(userEntity.email)
    }

    private fun saveChanges() {
        viewModel.setEventState(
            AccountEventState.UpdateAccountEvent(
                input_email.text.toString(),
                input_username.text.toString()
            )
        )
        uiCommunicationListener?.hideSoftKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> saveChanges()
        }
        return super.onOptionsItemSelected(item)
    }
}