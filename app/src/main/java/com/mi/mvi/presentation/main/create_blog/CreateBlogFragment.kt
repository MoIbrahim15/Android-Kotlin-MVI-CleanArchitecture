package com.mi.mvi.presentation.main.create_blog


import android.os.Bundle
import android.view.View
import com.mi.mvi.R
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.main.blog.viewmodel.BlogViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class CreateBlogFragment : BaseFragment(R.layout.fragment_create_blog) {

    private val createBlogViewModel: CreateBlogViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {

    }

}