package com.datatom.datrix3.Api


import com.datatom.datrix3.Bean.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

            @Header("ACCESS-TOKEN") token: String,

            @Field("dirid") dirid: String,

            @Field("userid") userid: String,

            @Field("orderby") orderby: String,

            @Field("sort") sort: String,

            @Field("page") page: Int,

            @Field("perpage") perpage: Int,

            @Field("fileordir") fileordir: Int,

            @Field("includetrash") includetrash: Boolean,

            @Field("isauth") isauth: Boolean


    ): Observable<PersonalFilelistData>


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

            @Header("ACCESS-TOKEN") token: String,

            @Field("dirid") dirid: String,

            @Field("userid") userid: String,

            @Field("orderby") orderby: String,

            @Field("sort") sort: String,

            @Field("page") page: Int,

            @Field("perpage") perpage: Int,

            @Field("fileordir") fileordir: Int,

            @Field("includetrash") includetrash: Boolean,

            @Field("isauth") isauth: Boolean


    ): Observable<PublicFilelistData>


    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("verify/code")
    fun getVerifyCode(@Header("ACCESS-TOKEN") token: String,

                      @Field("fileid") fileid: String
    ): Observable<VerifyCode>

    /**
     *
    dirname
    (required)
    目录名称

    formData	string
    userid
    创建者id。不传则从用户登录信息中获取

    formData	string
    parentuid
    父级目录创建者的用户id，父级目录为根目录，传空

    formData	string
    parentid
    父级目录id, 为空表示在个人目录根目录下创建

    formData	string
    parentobj
    父级目录的objectid，根目录为空，但是非根目录的时候一定要传

    formData	string
    isclient
    是否是datrix客户端调用，还是web网页上调用（默认web网页上调用，值为false）

    formData	boolean
     *
     *
     */
    @FormUrlEncoded
    @POST("persondir/create")
    fun createPersondir(
            @Header("ACCESS-TOKEN") token: String,
            @Field("dirname") dirname: String,

            @Field("userid") userid: String,

            @Field("parentuid") parentuid: String,

            @Field("parentid") parentid: String,

            @Field("parentobj") parentobj: String,

            @Field("isclient") isclient: Boolean


    ): Observable<CreateDir>


    /**
     * detrix 创建文件creat方法(1)
     *
     * @param filename
     * @return
     */
    @FormUrlEncoded
    @POST("file/create")
    fun FileCreate(
            @Header("ACCESS-TOKEN") token: String,
            @Field("filename") filename: String,
            @Field("filesize") filesize: String,
            @Field("createuid") createuid: String,
            @Field("parentuid") parentuid: String,
            @Field("dirid") dirid: String,
            @Field("parentobj") parentobj: String,
            @Field("isperdir") isperdir: Boolean,
            @Field("isclient") isclient: Boolean

    ):
            Observable<CreateFIie>


    /**
     * detrix 上传写入文件方法(2)
     *@Part MultipartBody.Part file
     * @return
     */
    @Multipart

    @POST("file/write")
    //@POST("http://192.168.3.217/api/cayman/store/object/write/file/write")
    fun FileWrite(
            @Header("ACCESS-TOKEN") token: String,

            @Part("object") objectid: String,
            @Part("offset") offset: String,
            @Part("length") length: String,
            @Part("bucket") bucket: String,
            @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>):

            Observable<String>

    @Multipart
    @POST("http://192.168.50.227/api/cayman/store/object/write/file/write")
    fun FileWrite2(
            @Header("ACCESS-TOKEN") token: String,
            @Part("object") objectid: String,
            @Part("offset") offset: String,
            @Part("length") length: String,
            @Part("bucket") bucket: String,
            @Part file: MultipartBody.Part):

            Observable<String>


    /**
     * detrix 上传结束方法(3)
     *
     * @param fileid
     * @param customjson
     * @return
     */
    @FormUrlEncoded
    @POST("file/finish")

    fun FileFinish(
            @Header("ACCESS-TOKEN") token: String,
            @Field("fileid") fileid: String,
            @Field("objectid") objectid: String,
            @Field("createuid") createuid: String
    ): Observable<String>


    /**
     * 获取用户配额
     */
    @FormUrlEncoded
    @POST("user/quota")
    fun userQuota(
           @Header("ACCESS-TOKEN") token : String,
           @Field("userid") userid : String

    ) : Observable<Quota>
}
