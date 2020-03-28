package com.mi.mvi.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.data.models.BlogPost

@Dao
interface BlogPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: BlogPost): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPostList(list: MutableList<BlogPost>)

    @Query("SELECT * FROM blog_post")
    suspend fun getAllBlogPosts(): MutableList<BlogPost>
}