package com.mi.mvi.features.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.mi.mvi.R
import com.mi.mvi.model.TokenView
import com.mi.mvi.features.auth.AuthActivity
import com.mi.mvi.base.BaseActivity
import com.mi.mvi.common.BOTTOM_NAV_BACKSTACK_KEY
import com.mi.mvi.common.BottomNavController
import com.mi.mvi.common.BottomNavController.*
import com.mi.mvi.common.setUpNavigation
import com.mi.mvi.features.main.account.AccountViewModel
import com.mi.mvi.features.main.account.ChangePasswordFragment
import com.mi.mvi.features.main.account.UpdateAccountFragment
import com.mi.mvi.features.main.blog.UpdateBlogFragment
import com.mi.mvi.features.main.blog.ViewBlogFragment
import com.mi.mvi.features.main.blog.viewmodel.BlogViewModel
import com.mi.mvi.features.main.create_blog.CreateBlogViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.getViewModel

const val AUTH_TOKEN_BUNDLE_KEY = "AUTH_TOKEN_BUNDLE_KEY"

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity(R.layout.activity_main),
    OnNavigationGraphChanged,
    OnNavigationReselectedListener {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var blogViewModel: BlogViewModel
    private lateinit var createBlogViewModel: CreateBlogViewModel

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.menu_nav_blog,
            this
        )
    }

    override fun onGraphChange() {
        app_bar.setExpanded(true)
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) {
        when (fragment) {
            is ViewBlogFragment -> {
                navController.navigate(R.id.action_viewBlogFragment_to_home)
            }

            is UpdateBlogFragment -> {
                navController.navigate(R.id.action_updateBlogFragment_to_home)
            }
            is UpdateAccountFragment -> {
                navController.navigate(R.id.action_updateAccountFragment_to_home)
            }
            is ChangePasswordFragment -> {
                navController.navigate(R.id.action_changePasswordFragment_to_home)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(tool_bar)
        setupBottomNavigationView(savedInstanceState)

        subscriberObservers()
        accountViewModel = getViewModel()
        blogViewModel = getViewModel()
        createBlogViewModel = getViewModel()

        restoreSession(savedInstanceState)
    }

    private fun restoreSession(savedInstanceState: Bundle?) {
        savedInstanceState?.let { inState ->
            inState[AUTH_TOKEN_BUNDLE_KEY]?.let { authToken ->
                sessionManager.setValue(authToken as TokenView)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            AUTH_TOKEN_BUNDLE_KEY,
            sessionManager.cachedTokenViewEntity.value
        )
        // save backstack for bottom nav
        outState.putIntArray(
            BOTTOM_NAV_BACKSTACK_KEY,
            bottomNavController.navigationBackStack.toIntArray()
        )
        super.onSaveInstanceState(outState)
    }

    private fun setupBottomNavigationView(savedInstanceState: Bundle?) {
        bottom_navigation_view.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.setupBottomNavigationBackStack(null)
            bottomNavController.onNavigationItemSelected()
        } else {
            (savedInstanceState[BOTTOM_NAV_BACKSTACK_KEY] as IntArray?)?.let { items ->
                val backStack = BackStack()
                backStack.addAll(items.toTypedArray())
                bottomNavController.setupBottomNavigationBackStack(backStack)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    private fun subscriberObservers() {
        sessionManager.cachedTokenViewEntity.observe(this, Observer { authToken ->
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    override fun displayLoading(isLoading: Boolean) {
        progress_bar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
