package com.datatom.datrix3.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.datatom.datrix3.Adapter.downlistadapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.UploadFileUtil.Companion.DOWNLOAD
import com.datatom.datrix3.Util.UploadFileUtil.Companion.UPLOAD
import com.datatom.datrix3.app
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class DownloadFragment : BaseFragment() {

    var rv: EasyRecyclerView? = null

    var downloadadapter: downlistadapter? = null

    private lateinit var database: AppDatabase

    override fun initview(view: View) {

        rv = view.find(I.download_rv)

        downloadadapter = downlistadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = downloadadapter
        }

        database = AppDatabase.getInstance(app.mapp)
        initdata()

    }

    private fun initdata() {

        database.TaskFileDao().queryUploadTaskFile(DOWNLOAD,Someutil.getUserID()).toString().LogD("downlist : ")
        downloadadapter!!.addAll(database.TaskFileDao().queryUploadTaskFile(DOWNLOAD,Someutil.getUserID()))


    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_downloadlist;
    }


}