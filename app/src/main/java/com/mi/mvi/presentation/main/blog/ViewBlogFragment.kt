package com.mi.mvi.presentation.main.blog

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mi.mvi.R
import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.presentation.AreYouSureCallBack
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.UIMessage
import com.mi.mvi.presentation.UIMessageType
import com.mi.mvi.presentation.main.blog.state.BLOG_VIEW_STATE_BUNDLE_KEY
import com.mi.mvi.presentation.main.blog.state.BlogEventState
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.presentation.main.blog.viewmodel.*
import com.mi.mvi.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_view_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class ViewBlogFragment : BaseBlogFragment(R.layout.fragment_view_blog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        checkIsAuthor()

        delete_button.setOnClickListener { deleteBlogPost() }
    }

    private fun deleteBlogPost() {
        uiCommunicationListener?.onUIMessageReceived(UIMessage(
            getString(R.string.are_you_sure_delete), UIMessageType.AreYouSureDialog(
                object : AreYouSureCallBack {
                    override fun proceed() {
                        viewModel.setEventState(BlogEventState.DeleteBlogPostEvent())
                    }

                    override fun cancel() {
                    }

                }
            )
        ))
    }

    private fun checkIsAuthor() {
        viewModel.setEventState(BlogEventState.CheckAuthorBlogPostEvent())
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                dataStateChangeListener?.onDataStateChangeListener(dataState = dataState)
                dataState.data?.let { data ->
                    data.data?.getContentIfNotHandled()?.let { viewState ->
                        viewModel.setIsAuthorOfBlogPost(
                            viewState.viewBlogFields.isAuthor
                        )
                    }

                    data.response?.peekContent()?.let { response ->
                        if (response.messageRes == R.string.delete) {
                            viewModel.removeDeleteBlogPost()
                            findNavController().popBackStack()
                        }

                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { blogViewState ->
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
        if (viewModel.isAuthorOfBlogPost()) {
            inflater.inflate(R.menu.edit_view_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (viewModel.isAuthorOfBlogPost()) {
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
        try {
            // prep for next fragment
            viewModel.setUpdatedBlogFields(
                viewModel.getBlogPost().title,
                viewModel.getBlogPost().body,
                viewModel.getBlogPost().image.toUri()
            )
            findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
        } catch (e: Exception) {
            // send error report or something. These fields should never be null. Not possible
        }
    }
}