package com.datatom.datrix3.Util


import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Util.Someutil.delFolder
import com.datatom.datrix3.app
import com.datatom.datrix3.base.DefaultProgressListener
import com.datatom.datrix3.base.UploadFileRequestBody
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

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
    private var taskFile: TaskFile? = null
    private var currentPieces = 0

    private lateinit var disposable  : Disposable

    companion object {
        val UPLOAD = 10
        val DOWNLOAD = 20
        val CREATING = 333
        val WRITING = 444
        val FINISHING = 555
        val DONE = 666
        val PAUSE = 44
        private val UPLOADLENGTH = 2 * 1024 * 1024

    }

    init {
        taskFile = file

        RxBus.get().toFlowable(this.javaClass).subscribe{


        }
    }

    fun doUpload() {
        initFile(taskFile!!)
    }

    private fun initFile(tfile: TaskFile) {

        currentPieces = 1
        val file = File(tfile.filePath)
        file.length().toString().LogD("length : ")
        tfile.apply {
            total = file.length()
            filename = file.name
            mCompeleteSize = 0L
            offset = 0
            filetype = UPLOAD
            filestate = CREATING
            userid = Someutil.getUserID()
            tfile.id = System.currentTimeMillis().toString()
            taskFile!!.id = System.currentTimeMillis().toString()
        }
        database.TaskFileDao().insert(tfile)

        CreateFile(tfile!!)
    }

    fun cancelUpload(){
        if (disposable!=null){
            disposable.dispose()
        }

    }

    fun ReUpload() {

        currentPieces = 1

        when(taskFile!!.filestate){
            CREATING ->{
                CreateFile(taskFile!!)
            }
            WRITING ->{
                WriteFile(taskFile!!)
            }
            FINISHING ->{
                UploadFinish(taskFile!!)
            }
        }



    }

    private fun CreateFile(tfile: TaskFile) {


        HttpUtil.instance.apiService().FileCreate(Someutil.getToken(), tfile.filename, tfile.total.toString(), Someutil.getUserID()
                , "", "8A85A16BB60D88F0", "", true, true)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())

                .subscribe({

                    when (it.code) {
                        200 -> {
                            it.toString().LogD("创建文件成功 : ")
                            tfile.apply {

                                fileid = it.reuslt.fileid
                                dirpath = it.reuslt.dirpath
                                objid = it.reuslt.objid
                                filestate = WRITING

                            }
                            //添加文件到数据库
                            database.TaskFileDao().updatefiles(tfile)

                            WriteFile(tfile)

                        }
                        else -> {
                            tfile.filestate = CREATING
                            database.TaskFileDao().updatefiles(tfile)
                            it.toString().LogD("创建文件失败 :code =!200 ")
                        }
                    }
                }, {
                    tfile.filestate = CREATING
                    database.TaskFileDao().updatefiles(tfile)

                    it.toString().LogD("创建文件失败 : ")

                })


    }

    private fun WriteFile(tfile: TaskFile) {

        val bodyMap = HashMap<String, RequestBody>()
        val file = File(tfile.filePath)
        val uploadlenght: Long
        if (tfile.total < tfile.offset + UPLOADLENGTH) {
            uploadlenght = tfile.total - tfile.offset

        } else {
            uploadlenght = UPLOADLENGTH.toLong()
        }

        Observable.just("split")
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    ("第" + currentPieces + "块文件上传").LogD()
                    var raf: RandomAccessFile?
                    try {
                        raf = RandomAccessFile(file, "r")
                        raf.seek(tfile.offset)
                        val buffer = ByteArray(uploadlenght.toInt())
                        raf.readFully(buffer)
                        val ins = ByteArrayInputStream(buffer)


                        val dir = File("/sdcard/tmp")
                        // 判断文件夹是否存在，不存在则创建
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }

                        val out = FileOutputStream(File(dir, "piece" + tfile.fileid))
                        val buf = ByteArray(1024)

                        while (true) {
                            val byteCount = ins.read(buf)
                            if (byteCount < 0) break
                            out.write(buf, 0, byteCount)

                        }

                        out.close()
                        ins.close()
                        val pfile = File("/sdcard/tmp/" + "piece" + tfile.fileid)


                        val fileRequestBody = UploadFileRequestBody(pfile, DefaultProgressListener(tfile))



                        bodyMap.put("file; filename=\"blob\"", fileRequestBody)
//

                         disposable = HttpUtil.instance.apiService().FileWrite(Someutil.getToken(), tfile.objid, tfile.offset.toString(),
                                uploadlenght.toString(), Someutil.getUserID(), bodyMap)
                                .compose(RxSchedulers.compose())

                                .subscribe({

                                    it.LogD("write result : ")
                                    if (it.contains("200")) {
                                        currentPieces += 1
                                        tfile.offset = tfile.offset + uploadlenght

                                        tfile.mCompeleteSize = tfile.offset
                                        database.TaskFileDao().updatefiles(tfile)


                                        if (tfile.mCompeleteSize === tfile.total) {

                                            tfile.filestate = FINISHING
                                            database.TaskFileDao().updatefiles(tfile)
                                            UploadFinish(tfile)
                                            ("go finish").LogD()

                                        } else {
                                            ("continue write").LogD()
                                            WriteFile(tfile)
                                        }


                                    }


                                }, {
                                    tfile.filestate = PAUSE
                                    database.TaskFileDao().updatefiles(tfile)
                                    it.toString().LogD("write http error : ")

                                    // WriteFile(tfile = tfile)
                                    // "重新传 ".LogD()


                                })
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

    private fun UploadFinish(tfile: TaskFile) {

        tfile.toString().LogD("file ::: ")

        HttpUtil.instance.apiService().FileFinish(Someutil.getToken(), tfile.fileid, tfile.objid, Someutil.getUserID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe({
                    if (it.contains("200")) {
                        tfile.filepersent = 100
                        tfile.filestate = DONE
                        database.TaskFileDao().updatefiles(tfile)
                        ("file 上传完毕！").LogD()
                        delFolder("/sdcard/tmp")
                    } else {

                        "dofinish失败！".LogD()
                    }
                }, {
                })


    }


}