package com.datatom.datrix3.Util

import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Service.TaskService.Companion.DONE
import com.datatom.datrix3.Service.TaskService.Companion.DOWNLOADING
import com.datatom.datrix3.Service.TaskService.Companion.PAUSE
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import io.reactivex.schedulers.Schedulers

/**
 * Created by wgz on 2018/3/12.
 */
class DownLoadUtil {

    var database = AppDatabase.getInstance(app.mapp).TaskFileDao()

    fun downloadFile(taskFile: TaskFile) {


        var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + taskFile.fileid + "&iswindows=0&optuser=admin"
        url.LogD("url : ")
        taskFile.taskstate = DOWNLOADING
        database.updatefiles(taskFile)

        HttpUtil.instance.downLoadApi(taskFile).downloadFileWithFixedUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe({

                    var result = Someutil.writeResponseBodyToDisk2(it, taskFile.fileid)
                    var file = result.second


                    if (result.first) {
                        "下载成功".LogD()
                        //  toast("下载成功")
                        taskFile.taskstate = DONE
                        taskFile.filePath = file.absolutePath
                        database.updatefiles(taskFile)

                    } else {
                        "下载失败".LogD()
                        taskFile.taskstate = PAUSE
                        database.updatefiles(taskFile)


                    }

                }, {
                    it.toString().LogD("download error : ")
                    taskFile.taskstate = PAUSE
                    database.updatefiles(taskFile)
                })

    }

    fun downfileNOProgress(taskFile: TaskFile){
        var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + taskFile.fileid + "&iswindows=0&optuser=admin"
        url.LogD("url : ")

        HttpUtil.instance.downLoadApi().downloadFileWithFixedUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe({


                    if (Someutil.writeResponseBodyToDisk(it, taskFile.fileid)) {
                        "下载成功".LogD()
                        //  toast("下载成功")

                    } else {
                        "下载失败".LogD()

                    }
                }, {
                    it.toString().LogD("download error : ")

                })



    }
}