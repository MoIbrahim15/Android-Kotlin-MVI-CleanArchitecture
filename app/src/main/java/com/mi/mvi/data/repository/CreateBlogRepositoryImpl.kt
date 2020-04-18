package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.datasource.model.BlogCreateUpdateResponse
import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogViewState
import com.mi.mvi.utils.DateUtils
import com.mi.mvi.utils.SuccessHandling.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.MessageType
import com.mi.mvi.utils.response_handler.StateMessage
import com.mi.mvi.utils.response_handler.UIComponentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class CreateBlogRepositoryImpl(
    private val blogRemoteDataSource: BlogRemoteDataSource,
    private val blogCacheDataSource: BlogCacheDataSource
) : CreateBlogRepository {
    override fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): Flow<DataState<CreateBlogViewState>> {
        return object :
            NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, CreateBlogViewState>(
                Dispatchers.IO,
                apiCall = {
                    blogRemoteDataSource.createBlog(
                        "Token ${authToken.token}",
                        title,
                        body,
                        image
                    )
                }) {

            override suspend fun updateCache(networkObject: BlogCreateUpdateResponse) {
                if (networkObject.response == RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER) {
                    val updateBlogPost = BlogPost(
                        networkObject.pk,
                        networkObject.title,
                        networkObject.slug,
                        networkObject.body,
                        networkObject.image,
                        DateUtils.convertServerStringDateToLong(networkObject.date_updated),
                        networkObject.username
                    )
                    blogCacheDataSource.insert(updateBlogPost)
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogCreateUpdateResponse): DataState<CreateBlogViewState>? {
                return if (response.response == RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER) {
                    DataState.ERROR(
                            StateMessage(
                                    "SUCCESS",
                                    UIComponentType.DIALOG,
                                    MessageType.SUCCESS
                            )
                    )
                } else null
            }


        }.result
    }
}