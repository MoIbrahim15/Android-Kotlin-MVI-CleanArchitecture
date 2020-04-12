package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mi.mvi.R
import com.mi.mvi.data.models.BlogPost
import com.mi.mvi.ui.AreYouSureCallBack
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.UIMessage
import com.mi.mvi.ui.UIMessageType
import com.mi.mvi.ui.main.blog.state.BlogEventState
import com.mi.mvi.ui.main.blog.viewmodel.*
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

        delete_button.setOnClickListener { deleteBlogPost() }
    }

    private fun deleteBlogPost() {
        uiCommunicationListener?.onUIMessageReceived(UIMessage(
            getString(R.string.are_you_sure_delete), UIMessageType.AreYouSureDialog(
                object : AreYouSureCallBack {
                    override fun proceed() {
                        blogViewModel.setEventState(BlogEventState.DeleteBlogPostEvent())
                    }

                    override fun cancel() {
                    }

                }
            )
        ))
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

                data.response?.peekContent()?.let { response ->
                    if (response.messageRes == R.string.delete) {
                        blogViewModel.removeDeleteBlogPost()
                        findNavController().popBackStack()
                    }

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
        try{
            // prep for next fragment
            blogViewModel.setUpdatedBlogFields(
                blogViewModel.getBlogPost().title,
                blogViewModel.getBlogPost().body,
                blogViewModel.getBlogPost().image.toUri()
            )
            findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
        }catch (e: Exception){
            // send error report or something. These fields should never be null. Not possible
        }
    }
}