package com.datatom.datrix3.listener

/**
 * Created by wgz on 2016/8/17.
 */

interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    fun onProgress(progress: Long, total: Long, done: Boolean)
}
