package com.datatom.datrix3.Api


import com.datatom.datrix3.Bean.LoginInfo
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

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


    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("login/login")
    fun login(@Field("userid") userid: String,
              @Field("password") password :String,
              @Field("type") type :String ,
              @Field("client") client :String

    ) :Observable<LoginInfo>

}
