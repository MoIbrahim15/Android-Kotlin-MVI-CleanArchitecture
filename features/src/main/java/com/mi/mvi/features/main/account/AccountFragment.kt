package com.mi.mvi.features.main.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.events.AccountEventState
import com.mi.mvi.model.UserView
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class AccountFragment : BaseAccountFragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        subscribeObservers()
        viewModel.setEventState(AccountEventState.GetAccountEvent)

        change_password.setOnClickListener { findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment) }
        logout_button.setOnClickListener { viewModel.logout() }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateChangeListener?.onDataStateChangeListener(dataState)
            when (dataState) {
                is DataState.SUCCESS -> {
                    dataState.data?.let { viewState ->
                        viewState.userEntity?.let { account ->
                            viewModel.setAccountData(userMapper.mapToView(account))
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.userEntity?.let { account ->
                setAccountAccount(userMapper.mapToView(account))
            }
        })
    }

    private fun setAccountAccount(account: UserView) {
        email.text = account.email
        username.text = account.username
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
