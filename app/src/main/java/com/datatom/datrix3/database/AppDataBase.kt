package com.datatom.datrix3.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.datatom.datrix3.Bean.SearchHis
import com.datatom.datrix3.Bean.User
import com.datatom.datrix3.database.Dao.SearchHisDao
import com.datatom.datrix3.database.Dao.UserDao

/**
 * Created by wgz on 2018/1/29.
 */

@Database(entities = [ (SearchHis::class),(User::class)], version = 1)
//@Database(entities = arrayOf(SearchHis::class,User::class), version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val DATABASE_NAME = "datrix-db"

        private var INSTANCE: AppDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, DATABASE_NAME
                    ).allowMainThreadQueries().build()
                }
                return INSTANCE as AppDatabase
            }
        }

    }

    abstract fun userDao(): UserDao

    abstract fun SearchHisDao() : SearchHisDao


}