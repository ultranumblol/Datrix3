package com.datatom.datrix3.Api


import com.datatom.datrix3.Bean.LoginInfo
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Bean.PublicFilelistData
import com.datatom.datrix3.Bean.VerifyCode
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
     * 登录
     */
    @FormUrlEncoded
    @POST("login/login")
    fun login(@Field("userid") userid: String,
              @Field("password") password: String,
              @Field("type") type: String,
              @Field("client") client: String

    ): Observable<LoginInfo>


    /**
     *
     * 列举个人目录子目录
     *
     * @param dirid 目录名，为空表示个人根目录. 个人目录ID:8A85A16BB60D88F0, 分享目录ID:5C59EBB7F9BDF00F,公共目录ID:1E07037D38FA20BB
     *
     * @param userid 文件所有者。不传则从用户登录信息中获取
     *
     * @param orderby 排序规则（先按type升序，再按时间降序）， 1. 日期'createtime', 2. 文件名'name'. 3.文件大小 ‘size’ 默认 'type,createtime'
     *
     * @param sort 倒序还是顺序 1. 倒序'desc', 2. 正序'asc'. 默认 'asc,desc'
     *
     * @param fileordir 列举文件还是目录，-1（文件和目录均列举）；0列举目录 1列举文件，默认-1
     *
     * @param includetrash 是否显示垃圾箱中的文件。默认值false：不显示垃圾箱中的文件
     *
     * @param isauth 是否列举权限元数据
     *
     */
    @FormUrlEncoded
    @POST("persondir/listdirfiles")

    fun persondir_listdirfiles(

            @Header("ACCESS-TOKEN") token : String,

            @Field("dirid") dirid: String,

            @Field("userid") userid: String,

            @Field("orderby") orderby: String,

            @Field("sort") sort: String,

            @Field("page") page: Int,

            @Field("perpage") perpage: Int,

            @Field("fileordir") fileordir: Int,

            @Field("includetrash") includetrash: Boolean,

            @Field("isauth") isauth: Boolean


    ) : Observable<PersonalFilelistData>


    /**
     *
     * 列举公共目录子目录
     *
     * @param dirid 目录名，为空表示个人根目录. 个人目录ID:8A85A16BB60D88F0, 分享目录ID:5C59EBB7F9BDF00F,公共目录ID:1E07037D38FA20BB
     *
     * @param userid 文件所有者。不传则从用户登录信息中获取
     *
     * @param orderby 排序规则（先按type升序，再按时间降序）， 1. 日期'createtime', 2. 文件名'name'. 3.文件大小 ‘size’ 默认 'type,createtime'
     *
     * @param sort 倒序还是顺序 1. 倒序'desc', 2. 正序'asc'. 默认 'asc,desc'
     *
     * @param fileordir 列举文件还是目录，-1（文件和目录均列举）；0列举目录 1列举文件，默认-1
     *
     * @param includetrash 是否显示垃圾箱中的文件。默认值false：不显示垃圾箱中的文件
     *
     * @param isauth 是否列举权限元数据
     *
     */
    @FormUrlEncoded
    @POST("pubdir/listdirfiles")
    fun pubdir_listdirfiles(

            @Header("ACCESS-TOKEN") token : String,

            @Field("dirid") dirid: String,

            @Field("userid") userid: String,

            @Field("orderby") orderby: String,

            @Field("sort") sort: String,

            @Field("page") page: Int,

            @Field("perpage") perpage: Int,

            @Field("fileordir") fileordir: Int,

            @Field("includetrash") includetrash: Boolean,

            @Field("isauth") isauth: Boolean


    ) : Observable<PublicFilelistData>


    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("verify/code")
    fun getVerifyCode(  @Header("ACCESS-TOKEN") token : String,

                        @Field("fileid") fileid: String
                        ) : Observable<VerifyCode>

}
