package com.mi.mvi.presentation.main.blog

import android.os.Bundle
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.main.blog.state.BLOG_VIEW_STATE_BUNDLE_KEY
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.presentation.main.blog.viewmodel.BlogViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
abstract class BaseBlogFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val viewModel: BlogViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { inState ->
            (inState[BLOG_VIEW_STATE_BUNDLE_KEY] as BlogViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        viewState?.blogFields?.blogList = ArrayList()
        outState.putParcelable(
            BLOG_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }
}