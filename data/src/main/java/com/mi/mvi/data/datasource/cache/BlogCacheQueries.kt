package com.mi.mvi.data.datasource.cache

import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.ORDER_BY_ASC_DATE_UPDATED
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.ORDER_BY_ASC_USERNAME
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.ORDER_BY_DESC_DATE_UPDATED
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.ORDER_BY_DESC_USERNAME
import com.mi.mvi.data.entity.BlogPostEntity

suspend fun BlogCacheDataSource.returnOrderedBlogQuery(
    filterAndOrder: String,
    query: String,
    page: Int
): MutableList<BlogPostEntity> {

    when {

        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED) -> {
            return searchBlogPostsOrderByDateDESC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED) -> {
            return searchBlogPostsOrderByDateASC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_DESC_USERNAME) -> {
            return searchBlogPostsOrderByAuthorDESC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_USERNAME) -> {
            return searchBlogPostsOrderByAuthorASC(
                query = query,
                page = page
            )
        }
        else ->
            return searchBlogPostsOrderByDateASC(
                query = query,
                page = page
            )
    }
}
