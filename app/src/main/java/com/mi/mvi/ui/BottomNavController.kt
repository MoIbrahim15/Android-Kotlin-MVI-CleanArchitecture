package com.mi.mvi.ui

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mi.mvi.R

class BottomNavController(
    val context: Context,
    @IdRes val containerId: Int,
    @IdRes val appStartDestinationID: Int,
    val graphChangeListener: OnNavigationGraphChanged?,
    val navGraphProvider: NavGraphProvider
) {
    private val TAG: String = "APP_DEBUG"
    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var navItemChangeListener: OnNavigationItemChanged
    private val navigationBackStack = BackStack.of(appStartDestinationID)

    init {
        if (context is Activity) {
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    fun onNavigationItemSelected(itemId: Int = navigationBackStack.last()): Boolean {
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            ).replace(containerId, fragment, itemId.toString())
            .addToBackStack(null)
            .commit()
        //add to backstack
        navigationBackStack.moveLast(itemId)

        //update checked icon
        navItemChangeListener.onItemChanged(itemId)

        //communicate with activity
        graphChangeListener?.onGraphChanged()

        return true
    }

    fun onBackPressed() {
        val childFragmentManager = fragmentManager.findFragmentById(containerId)!!
            .childFragmentManager

        when {
            childFragmentManager.popBackStackImmediate() -> {

            }
            //fragment backstack is empty so try to back on navigation stack
            navigationBackStack.size > 1 -> {
                //remove last item from backstack
                navigationBackStack.removeLast()
                //update the container with new fragment
                onNavigationItemSelected()
            }

            //if the stack has only one and it's not the navigation home we should ensure that the application always leave from startDestination
            navigationBackStack.last() != appStartDestinationID -> {
                navigationBackStack.removeLast()
                navigationBackStack.add(0, appStartDestinationID)
                onNavigationItemSelected()
            }

            else -> activity.finish()
        }
    }

    private class BackStack : ArrayList<Int>() {
        companion object {
            fun of(vararg elements: Int): BackStack {
                val b = BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(size - 1)

        fun moveLast(item: Int) {
            remove(item)
            add(item)
        }
    }

    //for setting the checked icon in the bottom nav
    interface OnNavigationItemChanged {
        fun onItemChanged(itemId: Int)
    }

    fun setOnNavigationItemChanged(listener: (itemId: Int) -> Unit) {
        this.navItemChangeListener = object : OnNavigationItemChanged {
            override fun onItemChanged(itemId: Int) {
                listener.invoke(itemId)
            }
        }
    }

    //get id for each graph
    //ex: r.navigation.nav_blog
    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId: Int): Int
    }

    //execute when navigation graph changea
    // select a new item in nav view as  ex: home->account
    interface OnNavigationGraphChanged {
        fun onGraphChanged()
    }

    interface OnNavigationReselectedListener {
        fun onReselectNavItem(navController: NavController, fragment: Fragment)
    }
}

fun BottomNavigationView.setUpNavigation(
    bottomNavController: BottomNavController,
    onReselectListener: BottomNavController.OnNavigationReselectedListener
) {
    setOnNavigationItemSelectedListener {
        bottomNavController.onNavigationItemSelected(it.itemId)
    }
    setOnNavigationItemReselectedListener {
        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let { fragment ->
            onReselectListener.onReselectNavItem(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
    }

    bottomNavController.setOnNavigationItemChanged { itemId ->
        menu.findItem(itemId).isChecked = true
    }
}