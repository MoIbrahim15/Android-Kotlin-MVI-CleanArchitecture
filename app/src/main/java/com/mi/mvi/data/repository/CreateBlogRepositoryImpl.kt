package com.mi.mvi.data.repository

import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogViewState
import com.mi.mvi.remote.entity.BlogPostResponse
import com.mi.mvi.utils.Constants.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
import com.mi.mvi.utils.DataState
import com.mi.mvi.utils.MessageType
import com.mi.mvi.utils.StateMessage
import com.mi.mvi.utils.UIComponentType
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
        authTokenEntity: AuthTokenEntity,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): Flow<DataState<CreateBlogViewState>> {
        return object :
            NetworkBoundResource<BlogPostResponse, BlogPostEntity, CreateBlogViewState>(
                Dispatchers.IO,
                apiCall = {
                    blogRemoteDataSource.createBlog(
                        "Token ${authTokenEntity.token}",
                        title,
                        body,
                        image
                    )
                }) {

            override suspend fun updateCache(networkObject: BlogPostResponse) {
                if (networkObject.response == RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER) {
                    val updateBlogPost =
                        BlogPostEntity(
                            networkObject.pk,
                            networkObject.title,
                            networkObject.slug,
                            networkObject.body,
                            networkObject.image,
                            networkObject.getDateAsLong(),
                            networkObject.username
                        )
                    blogCacheDataSource.insert(updateBlogPost)
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogPostResponse): DataState<CreateBlogViewState>? {
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
