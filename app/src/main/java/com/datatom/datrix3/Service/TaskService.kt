package com.datatom.datrix3.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.datatom.datrix3.Bean.BadgeNum
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Util.DownLoadUtil
import com.datatom.datrix3.Util.Someutil

import com.datatom.datrix3.Util.UploadFileUtil2
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by wgz on 2018/3/9.
 */
class TaskService : Service() {

    private var data: List<TaskFile>? = null
    private var appdatabase = AppDatabase.getInstance(app.mapp).TaskFileDao()


    companion object {

        private var disposeList = hashMapOf<String, Disposable>()

        val UPLOAD = 10
        val DOWNLOAD = 20
        val NORMALDOWNLOAD = 201
        val BEGDOWNLOAD = 202
        val CREATING = 333
        val WRITING = 444
        val FINISHING = 555
        val DONE = 666
        val PAUSE = 44
        val NEWFILE = 101
        val DOWNLOADING = 777
        fun addDispose(map: HashMap<String, Disposable>) {
            disposeList.putAll(map)
        }

        fun DisposedTask(taskid: String) {

            disposeList!!.forEach {
                if (it.key.equals(taskid)) {
                    it.value.dispose()
                    return@forEach
                }
            }
            disposeList.remove(taskid)

        }

    }

    override fun onCreate() {
        super.onCreate()

        Observable.interval(1, TimeUnit.SECONDS).
                compose(RxSchedulers.compose())
                .subscribe {

                    data = appdatabase.queryUploadTaskFile(UPLOAD, Someutil.getUserID()).sortedByDescending { it.id }

                    data!!.apply {

                        forEach {
                            // "offset : ${it.offset}".LogD()
                            // "file  :"+it.toString().LogD()
                            // "forcestop : ${it.forcestop}".LogD()
                            when (it.taskstate) {

                                NEWFILE -> {
                                    it.taskstate = WRITING
                                    UploadFileUtil2(it).doUpload()
                                }

                                PAUSE -> {
                                    if (it.forcestop) {
                                        //  " 不重上传".LogD()
                                    } else {
                                        it.taskstate = WRITING
                                        "继续上传".LogD()
                                        UploadFileUtil2(it).ReUpload()
                                    }
                                }
                                else -> {
                                    if (it.forcestop) {
                                        //" 不重上传".LogD()
                                    }

                                }
                            }

                        }
                    }
                }


        Observable.interval(1, TimeUnit.SECONDS).
                compose(RxSchedulers.compose())
                .subscribe {

                    data = appdatabase.queryUploadTaskFile(DOWNLOAD, Someutil.getUserID()).sortedByDescending { it.id }

                    data!!.apply {
                        //toString().LogD("task file info : ")
                        forEach {

                            when (it.taskstate) {

                                NEWFILE -> {
                                    DownLoadUtil().downloadFile(it)
                                }

                                PAUSE -> {
//

                                }
                                else -> {

                                }

                            }

                        }
                    }
                }


        Observable.interval(1, TimeUnit.SECONDS).
                compose(RxSchedulers.compose())
                .subscribe({
                    var num = (AppDatabase.getInstance(app.mapp).TaskFileDao().queryAllFile().size - AppDatabase.getInstance(app.mapp).TaskFileDao().queryAllUnDoneFile(TaskService.DONE).size)

//                    AppDatabase.getInstance(app.mapp).TaskFileDao().queryAllFile().size.toString().LogD(" allnum : ")
//                    AppDatabase.getInstance(app.mapp).TaskFileDao().queryAllUnDoneFile(TaskService.DONE).size.toString().LogD(" Donenum : ")
//
                    //num.toString().LogD("num : ")
//                    if (num > 0) {
//                        badeg.badgeNumber = num
//                    } else badeg.hide(true)
                    RxBus.get().post(BadgeNum(num))
                }, {
                    it.toString().LogD("num error : ")
                })

    }

    fun getdatalist(): List<TaskFile>? {

        return data
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}