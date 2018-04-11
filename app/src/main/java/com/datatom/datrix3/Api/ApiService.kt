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
   
    目录名称

  
    userid
    创建者id。不传则从用户登录信息中获取

  
    parentuid
    父级目录创建者的用户id，父级目录为根目录，传空

  
    parentid
    父级目录id, 为空表示在个人目录根目录下创建

  
    parentobj
    父级目录的objectid，根目录为空，但是非根目录的时候一定要传

  
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
            @Header("ACCESS-TOKEN") token: String,
            @Field("userid") userid: String

    ): Observable<Quota>

    /**
     * 删除文件 放入回收站
     */
    @FormUrlEncoded
    @POST("trash/do")
    fun trushDo(
            @Header("ACCESS-TOKEN") token: String,
            @Field("fileid") fileid: String,
            @Field("objectid") objectid: String,
            @Field("createuid") createuid: String

    ): Observable<String>

    /**
     * 文件重命名
     * type ： 重名处理类型（1（无重名）、 2（有，并覆盖）、 3、（有，但保留））
     */
    @FormUrlEncoded
    @POST("file/rename")
    fun filerename(
            @Header("ACCESS-TOKEN") token: String,
            @Field("newname") newname: String,
            @Field("fileid") fileid: String,
            @Field("createuid") createuid: String,
            @Field("objectid") objectid: String,
            @Field("parentid") parentid: String,
            @Field("type") type: String,
            @Field("ext") ext: String

    ): Observable<String>

    /**
     * 文件重命名
     * type ： 重名处理类型（1（无重名）、 2（有，并覆盖）、 3、（有，但保留））
     */
    @FormUrlEncoded
    @POST("dir/rename")
    fun dirrename(
            @Header("ACCESS-TOKEN") token: String,
            @Field("newname") newname: String,
            @Field("fileid") fileid: String,
            @Field("createuid") createuid: String,
            @Field("objectid") objectid: String,
            @Field("parentid") parentid: String,
            @Field("type") type: String,
            @Field("ext") ext: String

    ): Observable<String>

    /**
     * 获取文件基本信息
     */
    @FormUrlEncoded
    @POST("file/basicinfo")
    fun filebasicinfo(
            @Header("ACCESS-TOKEN") token: String,
            @Field("objectid") objectid: String,
            @Field("createuid") createuid: String,
            @Field("userid") userid: String


    ): Observable<FileBasicInfo>

    /**
     * 文件搜索
     */
    @FormUrlEncoded
    @POST("filesearch/simplesearch")
    fun filesimpleSearch(

            @Header("ACCESS-TOKEN") token: String,
            @Field("keyword") keyword: String,
            @Field("dirid") dirid: String,
            @Field("page") page: Int,
            @Field("perpage") perpage: Int,
            @Field("createuid") createuid: String,
            @Field("includetrash") includetrash: Boolean,
            @Field("userid") userid: String


    ): Observable<SearchResultData>


    /**
     * 文件移动
     */
    @FormUrlEncoded
    @POST("file/move")
    fun filemove(
            @Header("ACCESS-TOKEN") token: String,
            @Field("fileid") fileid: String,
            @Field("filename") filename: String,
            @Field("createuid") createuid: String,
            @Field("objectid") objectid: String,
            @Field("newparentid") newparentid: String,
            @Field("type") type: Int

    ): Observable<String>

    /**
     * 文件复制
     */
    @FormUrlEncoded
    @POST("file/copy")
    fun filecopy(

            @Header("ACCESS-TOKEN") token: String,
            @Field("createuid") createuid: String,
            @Field("objectid") objectid: String,
            @Field("fileid") fileid: String,
            @Field("newparentid") newparentid: String,
            @Field("isperdir") isperdir: Boolean,
            @Field("userid") userid: String

    ) : Observable<String>


    /**
     * 创建分享
     *
     *sharename
    分享链接名
  
    descript
    分享描述

    shareuid
    分享创建者ID

    username
    分享创建者用户名

    fileid
    文件ID

    istimer
    是否设置分享结束时间(用int 0和1代替bool)
   
    needpwd
    是否设置提取码(用int 0和1代替bool)
   
    sharename
    是否能下载(用int 0和1代替bool)

    canedit
    是否能修改(用int 0和1代替bool)

    edtime
    如果设置了分享时间,这里填写分享结束时间戳

    pubsee
    是否指定分享用户或组(用int 0和1代替bool)

    users
    指定分享用户列表
    
    groups
    指定分享组列表

    userid
    当前操作的用户userid

  
     *
     *
     */
    @FormUrlEncoded
    @POST("shares/linkcreate")
    fun linkcreate(
            @Header("ACCESS-TOKEN") token: String,
            @Field("sharename") sharename: String,
            @Field("descript") descript: String,
            @Field("shareuid") shareuid: String,
            @Field("username") username: String,
            @Field("fileid") fileid: String,
            @Field("istimer") istimer: Int,
            @Field("needpwd") needpwd: Int,
            @Field("candown") candown: Int,
            @Field("canedit") canedit: Int,
            @Field("edtime") edtime: String,
            @Field("pubsee") pubsee: Int,
            @Field("users") users: String,
            @Field("groups") groups: String,
            @Field("userid") userid: String

    ): Observable<CreatShare>

    /**
     * 展示该用户创建的分享链接列表
     */
    @FormUrlEncoded
    @POST("shares/linklist_create")
    fun linklist_create(
            @Header("ACCESS-TOKEN") token: String,
            @Field("userid") userid: String

    ): Observable<ShareList>

    /**
     * 展示所有的公共分享链接(包含需要提取码)列表
     */
    @FormUrlEncoded
    @POST("shares/linklist_public")
    fun linklist_public(
            @Header("ACCESS-TOKEN") token: String,
            @Field("userid") userid: String

    ): Observable<ShareList>

    /**
     * 删除链接分享
     */
    @FormUrlEncoded
    @POST("shares/linkdelete")
    fun linkdelete(
            @Header("ACCESS-TOKEN") token: String,
            @Field("shareid") shareid: String

    ): Observable<String>
    /**
     * 展示指定给该用户或用户组的分享链接列表
     */
    @FormUrlEncoded
    @POST("shares/linklist_pubsee")
    fun linklist_pubsee(
            @Header("ACCESS-TOKEN") token: String,
            @Field("userid") userid: String

    ): Observable<ShareList>

    /**
     *回收站列表
     */
    @FormUrlEncoded
    @POST("trash/list")
    fun trash_list(
            @Header("ACCESS-TOKEN") token: String,
            @Field("createuid") userid: String,
            @Field("page") page: Int,
            @Field("perpage") perpage: Int

    ): Observable<TrashBean>





}
