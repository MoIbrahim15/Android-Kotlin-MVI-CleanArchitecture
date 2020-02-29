package com.mi.mvi.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.data.models.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(account:Account) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(account:Account) : Long

    @Query("SELECT * FROM account WHERE pk = :pk")
    fun searchByPk(pk:Int) : Account?

    @Query("SELECT * FROM account WHERE email = :email")
    fun searchByEmail(email:String) : Account?
}