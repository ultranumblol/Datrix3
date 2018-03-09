package com.datatom.datrix3.database.Dao

import android.arch.persistence.room.*
import com.datatom.datrix3.Bean.SearchHis
import com.datatom.datrix3.Bean.User

/**
 * Created by wgz on 2018/1/29.
 */
@Dao
interface SearchHisDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(searchstr: SearchHis)


    @Query("SELECT * FROM searchhis")
    fun querySearchHis(): List<SearchHis>

    @Delete()
    fun  deleteHis(searchstr: SearchHis)

    @Query("DELETE FROM searchhis")
    fun nukeTable()

}