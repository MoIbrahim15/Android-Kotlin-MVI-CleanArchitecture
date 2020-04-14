package com.mi.mvi.presentation.main.blog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mi.mvi.R
import com.mi.mvi.presentation.BaseFragment
import com.mi.mvi.presentation.main.blog.state.BlogEventState
import com.mi.mvi.presentation.main.blog.viewmodel.BlogViewModel
import com.mi.mvi.presentation.main.blog.viewmodel.getUpdatedBlogUri
import com.mi.mvi.presentation.main.blog.viewmodel.onBlogPostUpdatedSuccess
import com.mi.mvi.presentation.main.blog.viewmodel.setUpdatedBlogFields
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogEventState
import com.mi.mvi.utils.Constants
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_blog.*
import kotlinx.android.synthetic.main.fragment_update_blog.*
import kotlinx.android.synthetic.main.fragment_update_blog.blog_body
import kotlinx.android.synthetic.main.fragment_update_blog.blog_image
import kotlinx.android.synthetic.main.fragment_update_blog.blog_title
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.io.File

@ExperimentalCoroutinesApi
class UpdateBlogFragment : BaseFragment(R.layout.fragment_update_blog) {

    private val viewModel: BlogViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        image_container.setOnClickListener{
            dataStateChangeListener?.let {
                if (it.isStoragePermissionGranted()) {
                    pickFromGallery()
                }
            }
        }
    }

    fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateChangeListener?.onDataStateChangeListener(dataState)
            dataState.data?.let { data ->
                data.data?.getContentIfNotHandled()?.let { viewState ->

                    // if this is not null, the blogpost was updated
                    viewState.viewBlogFields.blogPost?.let { blogPost ->
                        viewModel.onBlogPostUpdatedSuccess(blogPost)
                        findNavController().popBackStack()
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.updatedBlogFields.let { updatedBlogFields ->
                setBlogProperties(
                    updatedBlogFields.updatedBlogTitle,
                    updatedBlogFields.updatedBlogBody,
                    updatedBlogFields.updatedImageUri
                )
            }
        })
    }

    private fun setBlogProperties(title: String?, body: String?, image: Uri?) {
        Glide.with(this)
            .load(image)
            .into(blog_image)
        blog_title.setText(title)
        blog_body.setText(body)
    }

    private fun saveChanges() {
        var multipartBody: MultipartBody.Part? = null
        viewModel.getUpdatedBlogUri()?.let { imageUri ->
            imageUri.path?.let { filePath ->
                val imageFile = File(filePath)
                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                multipartBody =
                    MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
            }
        }

        multipartBody?.let { image ->
            viewModel.setEventState(
                BlogEventState.UpdateBlogPostEvent(
                    blog_title.text.toString(),
                    blog_body.text.toString(),
                    image
                )
            )

            dataStateChangeListener?.hideSoftKeyboard()
        } ?: showErrorDialog("")
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }


    private fun launchImageCrop(uri: Uri?) {
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        activity?.let {
                            launchImageCrop(uri)

                        }
                    } ?: showErrorDialog("")
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    viewModel.setUpdatedBlogFields(
                        title = null,
                        body = null,
                        uri = resultUri
                    )
                }
                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    showErrorDialog("")
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUpdatedBlogFields(
            uri = null,
            title = blog_title.text.toString(),
            body = blog_body.text.toString()
        )
    }
}