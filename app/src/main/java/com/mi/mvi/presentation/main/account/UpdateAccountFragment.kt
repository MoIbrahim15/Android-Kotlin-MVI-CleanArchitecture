package com.mi.mvi.presentation.main.account


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.main.account.state.AccountEventState
import kotlinx.android.synthetic.main.fragment_update_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class UpdateAccountFragment : BaseFragment(R.layout.fragment_update_account) {

    private val accountViewModel: AccountViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        accountViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateChangeListener?.onDataStateChangeListener(dataState)

        })

        accountViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.accountProperties?.let { accountProperties ->
                    setAccountProperties(accountProperties = accountProperties)
                }
            }
        })
    }

    private fun setAccountProperties(accountProperties: AccountProperties) {
        input_email.setText(accountProperties.email)
        input_username.setText(accountProperties.username)
        input_email.setText(accountProperties.email)
    }

    private fun saveChanges() {
        accountViewModel.setEventState(
            AccountEventState.UpdateAccountEvent(
                input_email.text.toString(),
                input_username.text.toString()
            )
        )
        dataStateChangeListener?.hideSoftKeyboard()
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