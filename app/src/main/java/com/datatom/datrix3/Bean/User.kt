package com.datatom.datrix3.Bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
/**
 * Created by wgz on 2018/1/29.
 */

@Entity(tableName = "user")
data class User(
        @ColumnInfo(name = "usernames")
        var username: String,


        @PrimaryKey
        @ColumnInfo(name = "id")

        val id: Long?

)