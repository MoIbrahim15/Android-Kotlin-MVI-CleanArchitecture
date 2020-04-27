package com.mi.mvi.features.main.blog

import android.os.Bundle
import com.mi.mvi.features.base.BaseFragment
import com.mi.mvi.domain.viewstate.BLOG_VIEW_STATE_BUNDLE_KEY
import com.mi.mvi.domain.viewstate.BlogViewState
import com.mi.mvi.features.main.blog.viewmodel.BlogViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseBlogFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {
    val viewModel: BlogViewModel by sharedViewModel()
}
