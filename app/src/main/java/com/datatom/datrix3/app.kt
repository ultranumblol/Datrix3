package com.datatom.datrix3

import android.app.Application
import android.content.Context
import com.datatom.datrix3.Util.HttpUtil
import com.danikula.videocache.HttpProxyCacheServer
import com.datatom.datrix3.helpers.LogD
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener




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
        QbSdk.setDownloadWithoutWifi(true)

        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
               "".LogD("QbSdk onCoreInitFinished::::  ")
            }

            override fun onViewInitFinished(b: Boolean) {

                b.toString().LogD("QbSdk onViewInitFinished::::  ")
                //这里被回调，并且b=true说明内核初始化并可以使用
                //如果b=false,内核会尝试安装，你可以通过下面监听接口获知
            }
        })

        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(i: Int) {
                //tbs内核下载完成回调
                i.toString().LogD("QbSdk onDownloadFinish : ")
            }

            override fun onInstallFinish(i: Int) {
                i.toString().LogD("QbSdk onInstallFinish : ")
                //内核安装完成回调，
            }

            override fun onDownloadProgress(i: Int) {
                i.toString().LogD("QbSdk onDownloadProgress : ")
                //下载进度监听
            }
        })




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