package com.mi.mvi.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.cache.model.CachedUser

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(cachedUser: CachedUser): Long

    @Query("SELECT * FROM account WHERE pk = :pk")
    suspend fun searchByPk(pk: Int): CachedUser?

    @Query("SELECT * FROM account WHERE email = :email")
    suspend fun searchByEmail(email: String): CachedUser?

    @Query("Update account Set email = :email, username = :username WHERE pk = :pk ")
    suspend fun updateAccountProperties(pk: Int, email: String?, username: String?)
}
