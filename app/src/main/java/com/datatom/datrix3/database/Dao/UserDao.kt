package com.datatom.datrix3.database.Dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.datatom.datrix3.Bean.User

/**
 * Created by wgz on 2018/1/29.
 */

@Dao
interface UserDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun  insert(user : User)

    @Query("SELECT * FROM user ")
    fun  queryUserInfo () : User

}