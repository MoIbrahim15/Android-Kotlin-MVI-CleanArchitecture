package com.mi.mvi.features.main.blog

import com.mi.mvi.base.BaseFragment
import com.mi.mvi.features.main.blog.viewmodel.BlogViewModel
import com.mi.mvi.mapper.BlogPostMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseBlogFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {
    val blogPostMapper = BlogPostMapper()
    val viewModel: BlogViewModel by sharedViewModel()
}
