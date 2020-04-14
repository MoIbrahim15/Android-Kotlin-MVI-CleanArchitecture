package com.mi.mvi.presentation.main.create_blog

import android.net.Uri
import androidx.lifecycle.LiveData
import com.mi.mvi.domain.usecase.blogs.CreateBlogUseCase
import com.mi.mvi.presentation.BaseViewModel
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogEventState
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogEventState.CreateNewBlogEvent
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogEventState.None
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogViewState
import com.mi.mvi.presentation.main.create_blog.state.NewBlogFields
import com.mi.mvi.utils.AbsentLiveData
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@ExperimentalCoroutinesApi
class CreateBlogViewModel(
    private val createBlogUseCase: CreateBlogUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel<CreateBlogEventState, CreateBlogViewState>() {

    override fun handleEventState(eventState: CreateBlogEventState): LiveData<DataState<CreateBlogViewState>> {
        return when (eventState) {
            is CreateNewBlogEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    val title = eventState.title
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val body = eventState.body
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    createBlogUseCase.invoke(
                        authToken,
                        title,
                        body,
                        eventState.image
                    )

                } ?: AbsentLiveData.create()
            }
            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): CreateBlogViewState {
        return CreateBlogViewState()
    }


    fun setNewBlogFields(title: String?, body: String?, uri: Uri?) {
        val update = getCurrentViewStateOrNew()
        val newBlogFields = update.newBlogField
        title?.let { newBlogFields.newBlogTitle = it }
        body?.let { newBlogFields.newBlogBody = it }
        uri?.let { newBlogFields.newImageUri = it }
        update.newBlogField = newBlogFields
        setViewState(update)
    }

    fun clearNewBlogFields() {
        val update = getCurrentViewStateOrNew()
        update.newBlogField = NewBlogFields()
        setViewState(update)
    }

    fun getNewImageUri(): Uri? {
        getCurrentViewStateOrNew().let { viewState ->
            viewState.newBlogField.let { newBlogFields ->
                return newBlogFields.newImageUri
            }
        }
    }
}