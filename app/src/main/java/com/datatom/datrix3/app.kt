package com.datatom.datrix3

import android.app.Application
import android.content.Context
import com.datatom.datrix3.Util.HttpUtil
import com.danikula.videocache.HttpProxyCacheServer


/**
 * Created by wgz on 2018/1/29.
 */
class app : Application() {

   // private var proxy: HttpProxyCacheServer? = null
    companion object {
        lateinit var mapp: app

    }

    override fun onCreate() {
        super.onCreate()
        mapp = this
       // proxy = newProxy()
        HttpUtil.instance.setContext(applicationContext)

    }

//    private fun newProxy(): HttpProxyCacheServer {
//        return HttpProxyCacheServer(this)
//    }
//    fun getProxy(context: Context): HttpProxyCacheServer {
//        if (proxy ==null){
//            proxy = newProxy()
//        }
//        return proxy as HttpProxyCacheServer
//
//    }
}