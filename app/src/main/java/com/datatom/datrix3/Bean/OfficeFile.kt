package com.datatom.datrix3.Bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.datatom.datrix3.Util.Someutil

/**
 * Created by wgz on 2018/1/29.
 */
@Entity(tableName = "officefile")
        data class OfficeFile(


        @ColumnInfo(name = "fileid")
        val fileid: String,

        @ColumnInfo(name = "filename")
        val filename: String,


        //当前进度
        @ColumnInfo(name = "progress")
        var progress: Int,



        @ColumnInfo(name = "totle")
        var totle: Int,



        @PrimaryKey
        @ColumnInfo(name = "id")

        val id: String



)