package com.datatom.datrix3.Api


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by wgz
 */

interface ApiService {


    @GET("/v2/book/{id}")
    fun getPushMsg(@Path("id") id: Int): Observable<ResponseBody>


    @GET("")
    fun addProduction(
            @Path("username") username: String,
            @Path("pwd") pwd: String

    ): Observable<String>

}
