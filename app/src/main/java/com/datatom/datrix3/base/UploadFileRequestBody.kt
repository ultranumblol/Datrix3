package com.datatom.datrix3.base


import com.datatom.datrix3.listener.ProgressListener

import java.io.File
import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import okio.Sink

/**
 * Created by wgz on 2016/11/9.
 */

class UploadFileRequestBody : RequestBody {

    private var mRequestBody: RequestBody? = null
    private var mProgressListener: ProgressListener? = null

    private var bufferedSink: BufferedSink? = null


    constructor(file: File, progressListener: ProgressListener) {
        this.mRequestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file)
        this.mProgressListener = progressListener
    }
    /*public UploadFileRequestBody(File file , ProgressListener progressListener, int pic) {
        this.mRequestBody = RequestBody.create(MediaType.parse("image*//*"), file) ;
        this.mProgressListener = progressListener ;
    }*/




    override fun contentType(): MediaType? {
        return mRequestBody!!.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mRequestBody!!.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink))
        }
        //写入
        mRequestBody!!.writeTo(bufferedSink!!)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            internal var bytesWritten = 0L
            //总字节长度，避免多次调用contentLength()方法
            internal var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调
                mProgressListener!!.onProgress(bytesWritten, contentLength, bytesWritten == contentLength)
            }
        }
    }
}
