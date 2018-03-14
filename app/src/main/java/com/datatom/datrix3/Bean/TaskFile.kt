package com.datatom.datrix3.Bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.datatom.datrix3.Util.Someutil
import java.io.Serializable

/**
 * Created by wgz on 2018/1/29.
 */
@Entity(tableName = "taskfile")
        data class TaskFile(


        @ColumnInfo(name = "fileid")
        var fileid: String = "",

        @ColumnInfo(name = "filename")
        var filename: String = "",

        @ColumnInfo(name = "filepersent")
        var filepersent: Int = 0,

        @ColumnInfo(name = "filesize")
        var filesize: String = "",

        @ColumnInfo(name = "filetype")
        var filetype: Int = 0,

        @ColumnInfo(name = "filePath")
        var filePath: String = "",

        @ColumnInfo(name = "filestate")
        var filestate: Int = 0,

        @ColumnInfo(name = "taskstate")
        var taskstate: Int = 0,

        @ColumnInfo(name = "mimetype")
        var mimetype: String = "",

        @ColumnInfo(name = "exe")
        var exe: String = "",

        @ColumnInfo(name = "forcestop")
        var forcestop: Boolean = false,

        @ColumnInfo(name = "dirpath")
        var dirpath: String = "",

        @ColumnInfo(name = "objid")
        var objid: String = "",

        @ColumnInfo(name = "dirid")
        var dirid: String = "",

        @ColumnInfo(name = "parentobj")
        var parentobj: String = "",

        @ColumnInfo(name = "userid")
        var userid: String = Someutil.getUserID(),

        //偏移量
        @ColumnInfo(name = "offset")
        var offset: Long  = 0,
        //当前进度
        @ColumnInfo(name = "mCompeleteSize")
        var mCompeleteSize: Long = 0,

        @ColumnInfo(name = "total")
        var total: Long  = 0,



        @PrimaryKey
        @ColumnInfo(name = "id")

        var id: String  = ""



) : Serializable