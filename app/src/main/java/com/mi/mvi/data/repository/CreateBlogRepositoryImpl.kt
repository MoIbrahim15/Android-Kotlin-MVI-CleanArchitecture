package com.mi.mvi.data.repository

import com.mi.mvi.R
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.datasource.model.BlogCreateUpdateResponse
import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.presentation.main.create_blog.state.CreateBlogViewState
import com.mi.mvi.utils.DateUtils
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.RESPONSE_CODINGWITHMITCH_MEMBER
import com.mi.mvi.utils.response_handler.ErrorHandler
import com.mi.mvi.utils.response_handler.Response
import com.mi.mvi.utils.response_handler.ResponseView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalCoroutinesApi
class CreateBlogRepositoryImpl(
    private val blogRemoteDataSource: BlogRemoteDataSource,
    private val blogCacheDataSource: BlogCacheDataSource,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler
) : CreateBlogRepository {
    override fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part
    ): Flow<DataState<CreateBlogViewState>> = flow {
        val networkBoundResource =
            object :
                NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, CreateBlogViewState>(
                    apiCall = {
                        blogRemoteDataSource.createBlog(
                            "Token ${authToken.token}",
                            title,
                            body,
                            image
                        )
                    },
                    cacheCall = null,
                    errorHandler = errorHandler,
                    canWorksOffline = false,
                    isNetworkAvailable = sessionManager.isConnectedToInternet()
                ) {
                override suspend fun handleNetworkSuccess(response: BlogCreateUpdateResponse) {

                    if (response.response == RESPONSE_CODINGWITHMITCH_MEMBER) {
                        val updateBlogPost = BlogPost(
                            response.pk,
                            response.title,
                            response.slug,
                            response.body,
                            response.image,
                            DateUtils.convertServerStringDateToLong(response.date_updated),
                            response.username
                        )
                        blogCacheDataSource.insert(updateBlogPost)
                        DataState.SUCCESS(Response(R.string.text_success, ResponseView.DIALOG()))

                    }
                }

                override suspend fun handleCacheSuccess(response: BlogPost?) {

                }

            }

        emitAll(networkBoundResource.call())
    }

}