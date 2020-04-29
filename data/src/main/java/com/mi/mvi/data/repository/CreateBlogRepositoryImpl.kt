package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.data.entity.BlogPostEntity
import com.mi.mvi.domain.Constants.Companion.SUCCESS_BLOG_CREATED
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.MessageType
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.domain.viewstate.CreateBlogViewState
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
        token: Token,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): Flow<DataState<CreateBlogViewState>> {
        return object :
            NetworkBoundResource<BlogPostEntity, BlogPostEntity, CreateBlogViewState>(
                Dispatchers.IO,
                apiCall = {
                    blogRemoteDataSource.createBlog(
                        "Token ${token.token}",
                        title,
                        body,
                        image
                    )
                }) {

            override suspend fun updateCache(networkObject: BlogPostEntity) {
                if (networkObject.response == SUCCESS_BLOG_CREATED) {
                    val updateBlogPost =
                        BlogPostEntity(
                            networkObject.pk,
                            networkObject.title,
                            networkObject.slug,
                            networkObject.body,
                            networkObject.image,
                            networkObject.date_updated,
                            networkObject.username
                        )
                    blogCacheDataSource.insert(updateBlogPost)
                }
            }

            override suspend fun handleNetworkSuccess(response: BlogPostEntity): DataState<CreateBlogViewState>? {
                return if (response.response == SUCCESS_BLOG_CREATED) {
                    DataState.ERROR(
                        StateMessage(
                            SUCCESS_BLOG_CREATED,
                            UIComponentType.DIALOG,
                            MessageType.SUCCESS
                        )
                    )
                } else null
            }
        }.result
    }
}
