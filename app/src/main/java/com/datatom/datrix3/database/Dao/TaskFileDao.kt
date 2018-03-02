package com.datatom.datrix3.database.Dao

import android.arch.persistence.room.*
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Bean.User

/**
 * Created by wgz on 2018/1/29.
 */

@Dao
interface TaskFileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(taskfile: TaskFile)

    @Query("SELECT * FROM taskfile ")
    fun queryUserInfo(): TaskFile

    @Delete()
    fun deleteHis(taskfile: TaskFile)

}