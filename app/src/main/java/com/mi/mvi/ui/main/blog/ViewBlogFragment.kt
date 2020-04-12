package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mi.mvi.R
import com.mi.mvi.data.models.BlogPost
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.main.blog.state.BlogEventState
import com.mi.mvi.ui.main.blog.viewmodel.BlogViewModel
import com.mi.mvi.ui.main.blog.viewmodel.isAuthorOfBlogPost
import com.mi.mvi.ui.main.blog.viewmodel.setIsAuthorOfBlogPost
import com.mi.mvi.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_view_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class ViewBlogFragment : BaseFragment(R.layout.fragment_view_blog) {

    private val blogViewModel: BlogViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        checkIsAuthor()
    }

    private fun checkIsAuthor() {
        blogViewModel.setEventState(BlogEventState.CheckAuthorBlogPostEvent())
    }

    private fun subscribeObservers() {
        blogViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateChangeListener?.onDataStateChangeListener(dataState = dataState)
            dataState.data?.let { data ->
                data.data?.getContentIfNotHandled()?.let { viewState ->
                    blogViewModel.setIsAuthorOfBlogPost(
                        viewState.viewBlogFields.isAuthor
                    )
                }
            }
        })

        blogViewModel.viewState.observe(viewLifecycleOwner, Observer { blogViewState ->
            blogViewState?.viewBlogFields?.blogPost?.let { blogPost ->
                setBlogProperties(blogPost)
            }

            if (blogViewState.viewBlogFields.isAuthor) {
                adaptViewToAuthorMode()
            }

        })

    }

    private fun adaptViewToAuthorMode() {
        activity?.invalidateOptionsMenu()
        delete_button.visibility = View.VISIBLE
    }

    private fun setBlogProperties(blogPost: BlogPost) {
        Glide.with(this)
            .load(blogPost.image)
            .into(blog_image)

        blog_title.text = blogPost.title
        blog_author.text = blogPost.username
        blog_update_date.text = DateUtils.convertLongToStringDate(blogPost.date_updated)
        blog_body.text = blogPost.body
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (blogViewModel.isAuthorOfBlogPost()) {
            inflater.inflate(R.menu.edit_view_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (blogViewModel.isAuthorOfBlogPost()) {
            when (item.itemId) {
                R.id.edit -> {
                    navUpdateBlogFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navUpdateBlogFragment() {
        findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
    }
}