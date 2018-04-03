package com.datatom.datrix3.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.datatom.datrix3.Activities.*
import com.datatom.datrix3.Adapter.downlistadapter
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.R
import com.datatom.datrix3.Service.TaskService
import com.datatom.datrix3.Service.TaskService.Companion.DONE
import com.datatom.datrix3.Service.TaskService.Companion.DOWNLOADING
import com.datatom.datrix3.Service.TaskService.Companion.NEWFILE
import com.datatom.datrix3.Service.TaskService.Companion.PAUSE
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.UploadFileUtil2

import com.datatom.datrix3.app
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.C
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.jude.easyrecyclerview.EasyRecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.find
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by wgz on 2018/1/23.
 */

class DownloadFragment : BaseFragment() {

    var rv: EasyRecyclerView? = null

    var downloadadapter: downlistadapter? = null

    private lateinit var database: AppDatabase

    private var data: List<TaskFile>? = null

    private var msubscription: CompositeDisposable? = null

    override fun initview(view: View) {

        rv = view.find(I.download_rv)

        downloadadapter = downlistadapter(activity!!)


        msubscription = CompositeDisposable()

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = downloadadapter
        }

        database = AppDatabase.getInstance(app.mapp)
        initdata()
        update()

    }

    private fun update() {

        var ob = Observable.interval(1, TimeUnit.SECONDS).
                compose(RxSchedulers.compose())
                .subscribe {

                    // "刷新数据 ".LogD()
                    data!!.forEach {
                        it.toString()
                        it.apply {

                            filepersent = database.TaskFileDao().queryTaskFile(it.id).filepersent
                            filePath = database.TaskFileDao().queryTaskFile(it.id).filePath

                            taskstate = database.TaskFileDao().queryTaskFile(it.id).taskstate

                        }
                        it.toString()

                    }

                    downloadadapter!!.notifyDataSetChanged()

                }

        msubscription!!.add(ob)

    }

    private fun initdata() {

        data = database.TaskFileDao().queryUploadTaskFile(UploadFileUtil2.DOWNLOAD, Someutil.getUserID()).sortedByDescending { it.id }

        downloadadapter!!.addAll(data)

        data.toString().LogD("downloadfilelist : ")

        downloadadapter!!.apply {

            setOnItemClickListener {


                when(allData[it].taskstate){
                    DONE ->{
                        when(allData[it].mimetype){
                            "image" -> {

                                val imglist = arrayListOf<String>(allData[it].fileid)

                                val bundle = Bundle()
                                bundle.apply {

                                    putInt("selet", 1)// 2 大图显示当前页数; 1,头像，不显示页数
                                    putInt("code", 1)//第几张
                                    putStringArrayList("imageuri", imglist)
                                }


                                context!!.startActivity(Intent(context, ViewBigImageActivity::class.java).putExtras(bundle).putExtra("file", allData[it]!!))


                            }

                        //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                            "video"
                            -> {


                                // rvadapter!!.allData[it].toString().LogD("click : ")

                                context!!.startActivity(Intent(context, PlayVideoActivity::class.java)
                                        .putExtra("file", allData[it]))


                            }

                        // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                            "audio"
                            -> {
                            }

                            "conf", "cpp", "htm", "html", "log", "sh", "xml"
                            -> {

                            }
                            "txt" ->{
                                context!!.startActivity(Intent(context, PreviewTXTFileActivity::class.java)
                                        .putExtra("file", allData[it]))


                            }
                            "zip" -> {

                            }
                            "pdf" -> {

                                context!!.startActivity(Intent(context, PDFViewerActivity::class.java)
                                        .putExtra("file", allData[it]))

                            }

                            "doc" -> {
                                context!!.startActivity(Intent(context, OfficeFileShowActivity::class.java)
                                        .putExtra("file", allData[it]))

                            }
                            else -> {


                            }


                        }

//                        context!!.startActivity(Intent(context, PlayVideoActivity::class.java)
//                                .putExtra("file", allData[it]))
                    }

                    DOWNLOADING -> {
                        var task =  AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(allData[it].id)
                        task.taskstate = PAUSE
                        AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(task)
                        TaskService.DisposedTask(allData[it].id)
                        notifyDataSetChanged()

                    }

                    PAUSE -> {
                        var task =  AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(allData[it].id)
                        task.taskstate = NEWFILE
                        AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(task)
                        notifyDataSetChanged()

                    }
                    else ->{

                    }
                }

            }

            setOnItemLongClickListener {
                AlertDialog.Builder(activity!!).run {
                    setTitle("删除任务")
                    setMessage("删除任务同时也会删除下载文件")
                    setPositiveButton("确认删除") { _, _ ->
                        AppDatabase.getInstance(app.mapp).TaskFileDao().deletefile(allData[it])

                        File(allData[it].filePath).delete()

                        remove(it)
                        data = allData

                        notifyDataSetChanged()


                    }
                    setNegativeButton("取消") { _, _ ->

                    }
                    show()

                }.apply {
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.red))
                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
                }

                return@setOnItemLongClickListener true
            }
        }


    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_downloadlist;
    }
    override fun onStop() {
        super.onStop()

        if (this.msubscription != null) {
            this.msubscription!!.dispose()
            "download取消订阅".LogD()
        }

    }

}