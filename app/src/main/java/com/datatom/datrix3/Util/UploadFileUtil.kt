package com.datatom.datrix3.Util

import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.app
import com.datatom.datrix3.base.DefaultProgressListener
import com.datatom.datrix3.base.UploadFileRequestBody
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.util.HashMap

/**
 * Created by wgz on 2018/3/6.
 */
class UploadFileUtil(file: TaskFile) {


    var database = AppDatabase.getInstance(app.mapp)
    private var tfile: TaskFile? = null
    private var currentPieces = 0



    companion object {
        val UPLOAD = 10
        val DOWNLOAD = 20
        val CREATING = 333
        val WRITING = 444
        val FINISHING = 555
        val DONE = 666
        val PAUSE = 44
        private val UPLOADLENGTH = 1 * 1024 * 1024

    }

    init {
        tfile = file!!

    }

    fun settaskFile(taskfile: TaskFile) {

        this.tfile = taskfile
    }

    fun doUpload() {
        initFile()
    }

    private fun initFile() {

        currentPieces = 1
        val file = File(tfile!!.filePath)
        file.length().toString().LogD("length : ")

        tfile!!.apply {
            total = file.length()
            filesize = file.length().toString()
            filename = file.name
            taskstate = CREATING
        }

        database.TaskFileDao().updatefiles(tfile!!)

        CreateFile()
    }

    fun ReUpload() {

        currentPieces = 1

        when (tfile!!.filestate) {
            CREATING -> {

                tfile!!.taskstate = WRITING
                database.TaskFileDao().updatefiles(tfile!!)
                CreateFile()
            }
            WRITING -> {
                tfile!!.taskstate = WRITING
                database.TaskFileDao().updatefiles(tfile!!)
                WriteFile()
            }
            FINISHING -> {
                tfile!!.taskstate = WRITING
                database.TaskFileDao().updatefiles(tfile!!)
                UploadFinish()
            }
        }
    }

    private fun CreateFile() {


        HttpUtil.instance.apiService().FileCreate(Someutil.getToken(), tfile!!.filename, tfile!!.total.toString(), Someutil.getUserID()
                , Someutil.getUserID(), tfile!!.dirid, tfile!!.parentobj, true, true)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())

                .subscribe({

                    when (it.code) {
                        200 -> {
                            it.toString().LogD("创建文件成功 : ")
                            tfile!!.apply {

                                fileid = it.reuslt.fileid
                                dirpath = it.reuslt.dirpath
                                objid = it.reuslt.objid
                                filestate = WRITING

                            }
                            //添加文件到数据库
                            database.TaskFileDao().updatefiles(tfile!!)

                            WriteFile()

                        }
                        else -> {
                            tfile!!.filestate = CREATING
                            tfile!!.taskstate = PAUSE
                            database.TaskFileDao().updatefiles(tfile!!)
                            it.toString().LogD("创建文件失败 :code =!200 ")
                        }
                    }
                }, {
                    tfile!!.filestate = CREATING
                    tfile!!.taskstate = PAUSE
                    database.TaskFileDao().updatefiles(tfile!!)

                    it.toString().LogD("创建文件失败 : ")

                })


    }

    private fun WriteFile() {

        var tfile2 = database.TaskFileDao().queryTaskFile(tfile!!.id)

        tfile2.toString().LogD("quary tfile2  ::::::::")

        val bodyMap = HashMap<String, RequestBody>()
        val file = File(tfile2!!.filePath)
        val uploadlenght: Long
        if (tfile2!!.total < tfile2!!.offset + UPLOADLENGTH) {
            uploadlenght = tfile2!!.total - tfile2!!.offset

        } else {
            uploadlenght = UPLOADLENGTH.toLong()
        }

//       var boo =  AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(tfile!!.id).forcestop
//        tfile!!.forcestop = boo
//        boo.toString().LogD(" 是否暂停 ： ")
//        if (boo){
//            tfile!!.taskstate = PAUSE
//            database.TaskFileDao().updatefiles(tfile!!)
//            "手动暂停".LogD()
//            return
//
//        }

        Observable.just("split")
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    ("第" + currentPieces + "块文件上传").LogD()
                    var raf: RandomAccessFile?
                    try {
                        raf = RandomAccessFile(file, "r")
                        raf.seek(tfile2!!.offset)
                        val buffer = ByteArray(uploadlenght.toInt())
                        raf.readFully(buffer)
                        val ins = ByteArrayInputStream(buffer)


                        val dir = File("/sdcard/tmp")
                        // 判断文件夹是否存在，不存在则创建
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }

                        val out = FileOutputStream(File(dir, "piece" + tfile2!!.fileid))
                        val buf = ByteArray(1024)

                        while (true) {
                            val byteCount = ins.read(buf)
                            if (byteCount < 0) break
                            out.write(buf, 0, byteCount)

                        }

                        out.close()
                        ins.close()
                        val pfile = File("/sdcard/tmp/" + "piece" + tfile2!!.fileid)


                        val fileRequestBody = UploadFileRequestBody(pfile, DefaultProgressListener(tfile))



                        bodyMap.put("file; filename=\"blob\"", fileRequestBody)
//

                        var disposable = HttpUtil.instance.apiService().FileWrite(Someutil.getToken(), tfile2!!.objid, tfile2!!.offset.toString(),
                                uploadlenght.toString(), Someutil.getUserID(), bodyMap)
                                .compose(RxSchedulers.compose())

                                .subscribe({

                                    it.LogD("write result : ")
                                    if (it.contains("200")) {
                                        currentPieces += 1
                                        tfile2!!.offset = tfile2!!.offset + uploadlenght

                                        tfile2!!.mCompeleteSize = tfile2!!.offset
                                        database.TaskFileDao().updatefiles(tfile2!!)


                                        if (tfile!!.mCompeleteSize === tfile!!.total) {

                                            tfile2!!.filestate = FINISHING
                                            database.TaskFileDao().updatefiles(tfile2!!)
                                            UploadFinish()
                                            ("go finish").LogD()

                                        } else {
                                            ("continue write").LogD()
                                            WriteFile()
                                        }


                                    } else {
                                        tfile2!!.taskstate = PAUSE
                                        database.TaskFileDao().updatefiles(tfile2!!)
                                    }


                                }, {


                                    tfile2!!.taskstate = PAUSE
                                    tfile2!!.forcestop = database.TaskFileDao().queryTaskFile(tfile2!!.id).forcestop
                                    database.TaskFileDao().updatefiles(tfile2!!)
                                    it.toString().LogD("write http error : ")

                                    // WriteFile(tfile = tfile)
                                    // "重新传 ".LogD()


                                })
                       // RxApiManager().get()!!.add(tfile!!.id,disposable)

//                        HttpUtil.instance.apiService2(UploadListener { bytesWritten, contentLength ->
//
////                            bytesWritten.toString().LogD("bytesWritten")
////                            contentLength.toString().LogD("contentLength")
//
//
//                        }).FileWrite2(Someutil.getToken(), tfile.objid, tfile.offset.toString(),
//                                uploadlenght.toString(), Someutil.getUserID(), body)
//                                .compose(RxSchedulers.compose())
//                                .subscribe({
//                                    it.LogD("write result : ")
//
//                                    if (it.contains("200")){
//                                        currentPieces += 1
//                                        tfile.offset = tfile.offset + uploadlenght
//
//                                        tfile.mCompeleteSize = tfile.offset
//
//                                        database.TaskFileDao().updatefiles(tfile)
//
//                                        if (tfile.mCompeleteSize === tfile.total) {
//                                            UploadFinish(tfile)
//                                            ("go finish").LogD()
//
//                                        } else {
//                                            ("continue write").LogD()
//                                            WriteFile(tfile)
//                                        }
//
//
//                                    }
//                                    else{
//
//                                        "write error ".LogD()
//                                    }
//
//
//                                }, {
//                                    it.toString().LogD("write http error : ")
//
//                                })


                    } catch (e: Exception) {
                        e.toString().LogD("write error : ")
                    }
                }


    }

    private fun UploadFinish() {
        var tfile2 = database.TaskFileDao().queryTaskFile(tfile!!.id)
        tfile2!!.toString().LogD("file ::: ")

        HttpUtil.instance.apiService().FileFinish(Someutil.getToken(), tfile2!!.fileid, tfile2!!.objid, Someutil.getUserID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe({
                    if (it.contains("200")) {
                        tfile2!!.filepersent = 100
                        tfile2!!.taskstate = DONE
                        database.TaskFileDao().updatefiles(tfile2!!)
                        ("file 上传完毕！").LogD()
                        Someutil.delFolder("/sdcard/tmp")
                    } else {
                        tfile2!!.taskstate = PAUSE
                        database.TaskFileDao().updatefiles(tfile2!!)
                        "dofinish失败！".LogD()
                    }
                }, {
                    tfile!!.taskstate = PAUSE
                    database.TaskFileDao().updatefiles(tfile2!!)
                })


    }





}