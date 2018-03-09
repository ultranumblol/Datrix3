package com.datatom.datrix3.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.UploadFileUtil
import com.datatom.datrix3.Util.UploadFileUtil2
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by wgz on 2018/3/9.
 */
class TaskService : Service() {

    private var data: List<TaskFile>? = null
    private var appdatabase = AppDatabase.getInstance(app.mapp).TaskFileDao()

    companion object {
        val UPLOAD = 10
        val DOWNLOAD = 20
        val CREATING = 333
        val WRITING = 444
        val FINISHING = 555
        val DONE = 666
        val PAUSE = 44
        val NEWFILE = 101


    }

    override fun onCreate() {
        super.onCreate()



        Observable.interval(1, TimeUnit.SECONDS).
                compose(RxSchedulers.compose())
                .subscribe {

                    data = appdatabase.queryUploadTaskFile(UploadFileUtil.UPLOAD, Someutil.getUserID()).sortedByDescending { it.id }

                    data!!.apply {
                        //toString().LogD("task file info : ")
                        forEach {

                            when (it.taskstate) {

                                NEWFILE -> {
                                    it.taskstate = WRITING
                                    UploadFileUtil2(it).doUpload()
                                }

                                PAUSE -> {
                                    it.taskstate = WRITING
                                    UploadFileUtil2(it).ReUpload()


                                }

                            }

                        }
                    }
                }


    }

    fun getdatalist(): List<TaskFile>? {

        return data
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}