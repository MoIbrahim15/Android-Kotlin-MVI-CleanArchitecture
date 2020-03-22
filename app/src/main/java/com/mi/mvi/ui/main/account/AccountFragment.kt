package com.mi.mvi.ui.main.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private val accountViewModel: AccountViewModel by currentScope.viewModel(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        change_password.setOnClickListener { findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment) }
        logout_button.setOnClickListener { accountViewModel.logout() }
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