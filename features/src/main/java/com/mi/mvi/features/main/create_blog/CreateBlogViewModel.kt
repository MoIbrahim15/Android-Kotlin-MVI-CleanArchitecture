package com.mi.mvi.features.main.create_blog

import android.net.Uri
import androidx.core.net.toUri
import com.mi.mvi.domain.usecase.blogs.CreateBlogUseCase
import com.mi.mvi.base.BaseViewModel
import com.mi.mvi.events.CreateBlogEventState
import com.mi.mvi.events.CreateBlogEventState.CreateNewBlogEvent
import com.mi.mvi.events.CreateBlogEventState.None
import com.mi.mvi.common.SessionManager
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.viewstate.CreateBlogViewState
import com.mi.mvi.domain.viewstate.NewBlogFields
import com.mi.mvi.mapper.TokenMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@FlowPreview
@ExperimentalCoroutinesApi
class CreateBlogViewModel(
    private val createBlogUseCase: CreateBlogUseCase,
    private val sessionManager: SessionManager,
    private val tokenMapper: TokenMapper

) : BaseViewModel<CreateBlogEventState, CreateBlogViewState>() {

    override fun handleEventState(eventState: CreateBlogEventState): Flow<DataState<CreateBlogViewState>> = flow {
         when (eventState) {
            is CreateNewBlogEvent -> {
                sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                    val title = eventState.title
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val body = eventState.body
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    createBlogUseCase.invoke(
                        tokenMapper.mapFromView(authToken),
                        title,
                        body,
                        eventState.image
                    )
                }
            }
            is None -> {

            }
        }
    }

    override fun initNewViewState(): CreateBlogViewState {
        return CreateBlogViewState()
    }

    fun setNewBlogFields(title: String?, body: String?, uri: Uri?) {
        val update = getCurrentViewStateOrNew()
        val newBlogFields = update.newBlogField
        title?.let { newBlogFields?.newBlogTitle = it }
        body?.let { newBlogFields?.newBlogBody = it }
        uri?.let { newBlogFields?.newImageUri = it.toString() }
        update.newBlogField = newBlogFields
        setViewState(update)
    }

    fun clearNewBlogFields() {
        val update = getCurrentViewStateOrNew()
        update.newBlogField =
            NewBlogFields()
        setViewState(update)
    }

    fun getNewImageUri(): Uri? {
        getCurrentViewStateOrNew().let { viewState ->
            viewState.newBlogField.let { newBlogFields ->
                return newBlogFields?.newImageUri?.toUri()
            }
        }
    }
}
