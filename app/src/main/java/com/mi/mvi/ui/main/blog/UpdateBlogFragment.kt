package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.view.View
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.main.blog.viewmodel.BlogViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class UpdateBlogFragment : BaseFragment(R.layout.fragment_update_blog){

    private val blogViewModel: BlogViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}