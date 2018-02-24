package com.datatom.datrix3

import android.app.Application
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.database.AppDatabase

/**
 * Created by wgz on 2018/1/29.
 */
class app : Application() {

    companion object {

            lateinit var mapp : app

    }

    override fun onCreate() {
        super.onCreate()
        mapp = this

        HttpUtil.instance.setContext(applicationContext)

    }

    fun getApp(): app {
        return mapp
    }

}