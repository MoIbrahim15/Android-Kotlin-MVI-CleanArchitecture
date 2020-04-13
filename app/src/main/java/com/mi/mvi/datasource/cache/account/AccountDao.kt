package com.mi.mvi.datasource.cache.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.datasource.model.AccountProperties

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("SELECT * FROM account WHERE pk = :pk")
    fun searchByPk(pk: Int): AccountProperties?

    @Query("SELECT * FROM account WHERE email = :email")
    fun searchByEmail(email: String): AccountProperties?

    @Query("Update account Set email = :email, username = :username WHERE pk = :pk ")
    fun updateAccountProperties(pk: Int, email: String, username: String)

}