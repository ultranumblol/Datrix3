package com.datatom.datrix3.Util

import android.content.Context
import java.io.File
import java.util.concurrent.TimeUnit

import com.datatom.datrix3.Api.ApiService

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by wgz on 2017/7/25.
 */

class HttpUtil {
    private var apiService: ApiService? = null
    private var context: Context? = null
    fun setContext(context: Context) {
        this.context = context
    }

    private fun defaultOkHttpClient(): OkHttpClient {

        val cacheFile = File(context!!.applicationContext.cacheDir.absolutePath, "HttpCache")
        val cacheSize = 10 * 1024 * 1024
        val cache = Cache(cacheFile, cacheSize.toLong())
        val okBuilder = OkHttpClient.Builder()
        //okBuilder.cache(cache)
        okBuilder.readTimeout(20, TimeUnit.SECONDS)
        okBuilder.connectTimeout(10, TimeUnit.SECONDS)
        okBuilder.writeTimeout(20, TimeUnit.SECONDS)
        val client = okBuilder.build()

        // val client = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        //        .build();


        return client
    }

    fun apiService(): ApiService {

        if (apiService == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(defaultOkHttpClient())
                    .build()
            apiService = retrofit.create(ApiService::class.java)
        }


        return apiService!!
    }

    companion object {
        private var sHttpUtils: HttpUtil? = null

       var BASE_URL = "http://192.168.3.217/api/sw/"

        var BASEAPI_URL = "http://192.168.3.217/"

        val instance: HttpUtil
            get() {
                if (sHttpUtils == null) {
                    sHttpUtils = HttpUtil()
                }


                return sHttpUtils as HttpUtil
            }
    }

}
