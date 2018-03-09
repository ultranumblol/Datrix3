package com.datatom.datrix3.database.Dao

import android.arch.persistence.room.*
import com.datatom.datrix3.Bean.OfficeFile
import com.datatom.datrix3.Bean.TaskFile

/**
 * Created by wgz on 2018/1/29.
 */

@Dao
interface OfficeFileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(OfficeFile: OfficeFile)

    @Query("SELECT * FROM officefile WHERE id = :id")
    fun queryofficeifle(id : String): OfficeFile

    @Delete()
    fun deleteofficeifle(officeFile: OfficeFile)


    @Update
    fun updatefiles(vararg files: OfficeFile)

    @Query("DELETE FROM officefile")
    fun nukeTable()

}