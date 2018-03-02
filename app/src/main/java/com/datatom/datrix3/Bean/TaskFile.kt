package com.datatom.datrix3.Bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by wgz on 2018/1/29.
 */
@Entity(tableName = "taskfile")
        data class TaskFile(


        @ColumnInfo(name = "fileid")
        val fileid: String,

        @ColumnInfo(name = "filename")
        val filename: String,

        @ColumnInfo(name = "filepersent")
        val filepersent: String,

        @ColumnInfo(name = "filesize")
        val filesize: String,

        @ColumnInfo(name = "filetype")
        val filetype: String,

        @ColumnInfo(name = "filePath")
        val filePath: String,

        @ColumnInfo(name = "filestate")
        val filestate: String,


        //偏移量
        @ColumnInfo(name = "offset")
        val offset: Long,
        //当前进度
        @ColumnInfo(name = "mCompeleteSize")
        val mCompeleteSize: Long,

        @ColumnInfo(name = "total")
        val total: Long,



        @PrimaryKey
        @ColumnInfo(name = "id")

        val id: Long?



)