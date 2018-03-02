package com.datatom.datrix3.Api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Streaming
import retrofit2.http.Url


/**
 * Created by wgz on 2017/5/9.
 */

interface DownLoadApi {
    /**
     * 下载
     * @param fileUrl
     * @return
     */
    @GET
    @Streaming
    @Headers("Accept-Encoding:identity")
    fun downloadFileWithFixedUrl(@Url fileUrl: String): Observable<ResponseBody>

}
