package com.datatom.datrix3.fragments

import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.datatom.datrix3.Adapter.uploadadapter
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.R
import com.datatom.datrix3.Service.TaskService.Companion.DONE
import com.datatom.datrix3.Service.TaskService.Companion.PAUSE
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.UploadFileUtil2

import com.datatom.datrix3.app
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.C
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus
import com.jude.easyrecyclerview.EasyRecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.find
import java.util.concurrent.TimeUnit

/**
 * Created by wgz on 2018/1/23.
 */

class UploadFragment : BaseFragment() {
    var rv: EasyRecyclerView? = null

    var uploadadapter: uploadadapter? = null

    private lateinit var database: AppDatabase

    private var data: List<TaskFile>? = null

    private var msubscription: CompositeDisposable? = null

    override fun initview(view: View) {

        rv = view.find(I.upload_rv)

        uploadadapter = uploadadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = uploadadapter
        }

        database = AppDatabase.getInstance(app.mapp)
        msubscription = CompositeDisposable()
        initdata()
        update()
        RxBus.get().toFlowable(String::class.java)
                .subscribe {
                    when(it){
                        "clearuploadlist" ->{
                            clearlist()

                        }
                    }

                }

    }

    private fun clearlist() {
        "删除上传列表".LogD()
//        uploadadapter!!.allData.forEach {
//            AppDatabase.getInstance(app.mapp).TaskFileDao().deletefile(it)
//        }

        uploadadapter!!.allData.forEach {
            AppDatabase.getInstance(app.mapp).TaskFileDao().deletefile(it)
            uploadadapter!!.remove(it)
            data = uploadadapter!!.allData
            uploadadapter!!.notifyDataSetChanged()
        }

    }

    private fun initdata() {

        data = database.TaskFileDao().queryUploadTaskFile(UploadFileUtil2.UPLOAD, Someutil.getUserID()).sortedByDescending { it.id }



        uploadadapter!!.addAll(data)

        data.toString().LogD("uploadfilelist : ")

        uploadadapter!!.apply {

            setOnItemClickListener {


                when (allData[it].taskstate) {
                    DONE -> {

                    }

                    else ->{
                       when(allData[it].forcestop){

                           true -> {
                               "继续" .LogD()
                               var task =  AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(allData[it].id)
                               task.forcestop = false
                               allData[it].forcestop = false
                               AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(task)
                               notifyDataSetChanged()
                           }

                           false -> {

                               //allData[it].taskstate = PAUSE
                               "暂停" .LogD()
                               var task =  AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(allData[it].id)
                               task.forcestop = true
                               task.taskstate = PAUSE
                               allData[it].forcestop = true
                               allData[it].taskstate = PAUSE
                               AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(task)
                               notifyDataSetChanged()
                           }

                       }
                    }
                }


            }

            setOnItemLongClickListener {

                //"changan".LogD()
                AlertDialog.Builder(activity!!).run {
                    setTitle("从任务列表删除")
                    setPositiveButton("确认删除") { _, _ ->
                        AppDatabase.getInstance(app.mapp).TaskFileDao().deletefile(allData[it])

                        uploadadapter!!.apply {
                            remove(it)
                            data = allData
                            notifyDataSetChanged()
                        }


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

    private fun update() {

        var ob = Observable.interval(1, TimeUnit.SECONDS).
                compose(RxSchedulers.compose())
                .subscribe {

                    // "刷新数据 ".LogD()
                    data!!.forEach {
                        it.toString()
                        it.apply {
                            filepersent = database.TaskFileDao().queryTaskFile(it.id).filepersent
                            filestate = database.TaskFileDao().queryTaskFile(it.id).filestate
                            taskstate = database.TaskFileDao().queryTaskFile(it.id).taskstate


                        }
                        it.toString()

                    }

                    uploadadapter!!.notifyDataSetChanged()

                }

        msubscription!!.add(ob)

    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_uploadlist;
    }

    override fun onStop() {
        super.onStop()

        if (this.msubscription != null) {
            this.msubscription!!.dispose()
            //"取消订阅".LogD()
        }

    }


}