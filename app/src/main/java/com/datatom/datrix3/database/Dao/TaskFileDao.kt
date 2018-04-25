package com.datatom.datrix3.database.Dao

import android.arch.persistence.room.*

import com.datatom.datrix3.Bean.TaskFile


/**
 * Created by wgz on 2018/1/29.
 */

@Dao
interface TaskFileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(taskfile: TaskFile)

    @Query("SELECT * FROM taskfile  WHERE filetype = :filetype AND userid = :userid")
    fun queryUploadTaskFile(filetype : Int,userid :String):  List<TaskFile>

    @Query("SELECT * FROM taskfile  WHERE id = :id")
    fun queryTaskFile(id : String):  TaskFile

    @Query("SELECT * FROM taskfile  WHERE taskstate = :taskstate")
    fun queryAllUnDoneFile(taskstate : Int):  List<TaskFile>


    @Query("SELECT * FROM taskfile  ")
    fun queryAllFile():  List<TaskFile>

    @Delete()
    fun deletefile(taskfile: TaskFile)

    @Update
    fun updatefiles(vararg files: TaskFile)

    @Query("DELETE FROM taskfile")
    fun nukeTable()
}