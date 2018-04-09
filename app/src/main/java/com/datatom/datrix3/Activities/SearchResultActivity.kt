package com.datatom.datrix3.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import collections.forEach
import com.datatom.datrix3.Adapter.SearchAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.SpacePageList
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Service.TaskService
import com.datatom.datrix3.Service.TaskService.Companion.NORMALDOWNLOAD

import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_search_result.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


class SearchResultActivity : BaseActivity(), View.OnClickListener {

    private var rvadapter: SearchAdapter? = null
    private var currentdirid = ""
    private var currentdirname = ""
    private var keyword = ""
    private var pagelist: ArrayList<SpacePageList> = arrayListOf()

    private var cpge = 1
    override fun ActivityLayout(): Int {

        return R.layout.activity_search_result
    }

    override fun initView() {
        rv_searchresult.setLayoutManager(LinearLayoutManager(this))
        rvadapter = SearchAdapter(this)
        rv_searchresult.adapter = rvadapter
        tool_bar.hide()
        edit_ll!!.hide()
        edit_more_ll!!.hide()
        et_search.setText(intent.getStringExtra("keyword"))
        et_search!!.setOnEditorActionListener { _, i, _ ->

            if (i == EditorInfo.IME_ACTION_SEARCH) {

                //goSearch(mEtSearch.getText().toString())

                if (!et_search!!.text.toString().isEmpty()) {
                    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    keyword = et_search!!.text.toString()
                    initdata(keyword)
                    true
                }
            }
            false
        }
        edit_ll.setOnClickListener(this)
        edit_more_ll.setOnClickListener(this)
        edit_rv_rename.setOnClickListener(this)
        edit_rv_xiazai.setOnClickListener(this)
        edit_rv_share.setOnClickListener(this)
        edit_rv_delete.setOnClickListener(this)
        edit_rv_dabaoxiazai.setOnClickListener(this)
        edit_rv_copy.setOnClickListener(this)
        edit_rv_move.setOnClickListener(this)
        edit_rv_detil.setOnClickListener(this)

        iv_search_back.setOnClickListener {
            onBackPressed()
        }
        pagelist = arrayListOf()
        currentdirid = intent.getStringExtra("dirid")
        keyword = intent.getStringExtra("keyword")
        pagelist.add(SpacePageList(currentdirid, "", 1, true, ""))


        rvadapter!!.apply {
            setMore(L.view_more, {
                Handler().postDelayed({
                    cpge++

                    rvadapter!!.setCheckBoxNoneSelect()
                    rvadapter!!.notifyDataSetChanged()

                    initdata(keyword)

                }, 1000)

            })

            setOnItemClickListener {
                if (allData[it].cayman_pretreat_mimetype != null)
                    allData[it].cayman_pretreat_mimetype?.LogD("click  cayman_pretreat_mimetype : ")
                if (allData[it].ext != null)
                    allData[it].ext?.LogD("click  ext : ")

                if (getcheckboxArrary().size() > 0) {
                    setItemChecked(it)

                } else {
                    when (rvadapter!!.allData[it].type) {
                        "0" -> {
                            rvadapter!!.allData[it].fileid

                            var sp = SpacePageList(rvadapter!!.allData[it].fileid, rvadapter!!.allData[it].filename, 1, false, rvadapter!!.allData[it].objid)
                            cpge = 1
                            pagelist.add(sp)
                            pagelist.toString().LogD("after add pagelist : ")
                            currentdirname = rvadapter!!.allData[it].filename
                            currentdirid = rvadapter!!.allData[it].fileid
                            showpageback()
                            rvadapter!!.clear()
                            initdata(keyword)


                        }
                        "1" -> {
                            if (rvadapter!!.allData[it].cayman_pretreat_mimetype != null)
                                when (rvadapter!!.allData[it].cayman_pretreat_mimetype) {
                                    "image" -> {

                                        val imglist = arrayListOf<String>(rvadapter!!.allData[it].fileid)

                                        val bundle = Bundle()
                                        bundle.apply {

                                            putInt("selet", 1)// 2 大图显示当前页数; 1,头像，不显示页数
                                            putInt("code", 1)//第几张
                                            putStringArrayList("imageuri", imglist)
                                        }
                                        context!!.startActivity(Intent(context, ViewBigImageActivity::class.java).putExtras(bundle).putExtra("file", rvadapter!!.allData[it]!!))
                                    }

                                //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                                    "video"
                                    -> {
                                        // rvadapter!!.allData[it].toString().LogD("click : ")
                                        context!!.startActivity(Intent(context, PlayVideoActivity::class.java)
                                                .putExtra("file", rvadapter!!.allData[it]))
                                    }

                                // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                                    "audio"
                                    -> {
                                    }

                                    "conf", "cpp", "htm", "html", "log", "sh", "xml"
                                    -> {

                                    }
                                    "txt" -> {
                                        context!!.startActivity(Intent(context, PreviewTXTFileActivity::class.java)
                                                .putExtra("file", rvadapter!!.allData[it]))
                                    }
                                    "zip" -> {

                                    }
                                    "pdf" -> {

                                        context!!.startActivity(Intent(context, PDFViewerActivity::class.java)
                                                .putExtra("file", rvadapter!!.allData[it]))

                                    }
                                    "doc" -> {
                                        context!!.startActivity(Intent(context, OfficeFileShowActivity::class.java)
                                                .putExtra("file", rvadapter!!.allData[it]))
                                    }
                                    else -> {

                                    }
                                }
                        }
                    }
                }

            }


        }

        initdata(keyword)
        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)

        RxBus.get().toFlowable(String::class.java).subscribe {
            when (it) {
                "updateSearchcheckbox" -> {
                    updatecheckBox()

                }
            }
        }

    }

    private fun updatecheckBox() {
        if (rvadapter!!.getcheckboxArrary().size() > 0) {

            edit_ll!!.Show()


            rvadapter!!.notifyDataSetChanged()


        } else {

            edit_ll!!.hide()
            edit_more_ll!!.hide()
            rvadapter!!.notifyDataSetChanged()
        }


    }

    private fun showpageback() {

        tool_bar.Show()
        setToolbartitle(currentdirname)
        cardView_search.hide()


    }

    private fun hidepageback() {

        tool_bar.hide()
        setToolbartitle(currentdirname)
        cardView_search.Show()


    }

    fun goback() {
        cpge = 1
        if (pagelist.size > 1)
            pagelist.removeAt(pagelist.size - 1)
        //pagelist.size.toString().LogD(" after remove list  : ")
        currentdirid = pagelist[pagelist.size - 1].fileid
        rvadapter!!.clear()
        initdata(keyword)
        if (pagelist.size == 1) hidepageback()


    }

    override fun onBackPressed() {

        if (pagelist.size == 1)
            super.onBackPressed()
        else goback()

    }

    override fun onClick(v: View?) {

        when (v) {
            edit_ll -> {
                edit_more_ll!!.visibility =
                        if (edit_more_ll!!.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE
            }
            edit_ll -> {

            }
            edit_more_ll -> {
            }
            edit_rv_rename -> {
                var editview2 = View.inflate(this, R.layout.dialog_edittext_dabaoxiazai, null)
                editview2.find<TextView>(I.dialog_edittext).hint = "请输入文件名称"

                if (rvadapter!!.getcheckboxArrary().size() == 1)
                    this.let {

                        AlertDialog.Builder(it!!).run {
                            setView(editview2)
                            setTitle("重命名")
                            setNegativeButton("取消") { _, _ ->

                            }
                            setPositiveButton("确认") { _, _ ->

                                when (rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].type) {
                                //文件夹重命名
                                    "0" -> {
                                        HttpUtil.instance.apiService().dirrename(Someutil.getToken(), editview2.find<TextView>(I.dialog_edittext).text.toString()
                                                , rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].fileid,
                                                rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].createuid,
                                                rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].objid,
                                                rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].parentid,
                                                "1", rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].ext)
                                                .compose(RxSchedulers.compose())
                                                .subscribe({

                                                    it.toString().LogD("rename : ")
                                                    if (it.contains("200"))
                                                        this@SearchResultActivity!!.toast("重命名成功！")
                                                    rvadapter!!.setCheckBoxNoneSelect()
                                                    updatecheckBox()
                                                    Handler().postDelayed({
                                                        refreshData()
                                                    }, 1000)
                                                }, {
                                                    it.toString().LogD("error : ")
                                                    this@SearchResultActivity!!.toast("重命名失败！")
                                                })
                                    }
                                //文件重命名
                                    "1" -> {
                                        HttpUtil.instance.apiService().filerename(Someutil.getToken(), editview2.find<TextView>(I.dialog_edittext).text.toString()
                                                , rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].fileid,
                                                rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].createuid,
                                                rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].objid,
                                                rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].parentid,
                                                "1", rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)].ext)
                                                .compose(RxSchedulers.compose())
                                                .subscribe({

                                                    it.toString().LogD("rename : ")
                                                    if (it.contains("200"))
                                                        this@SearchResultActivity!!.toast("重命名成功！")
                                                    rvadapter!!.setCheckBoxNoneSelect()
                                                    updatecheckBox()
                                                    Handler().postDelayed({
                                                        refreshData()
                                                    }, 1000)
                                                }, {
                                                    it.toString().LogD("error : ")
                                                    this@SearchResultActivity!!.toast("重命名失败！")
                                                })

                                    }
                                }


                            }
                            show()
                        }.apply {
                            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
                            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
                        }
                    }


            }
            edit_rv_xiazai -> {
                rvadapter!!.getcheckboxArrary().forEach { i, _ ->

                    var taskfile = TaskFile()
                    taskfile!!.apply {

                        filename = rvadapter!!.allData[i].filename
                        fileid = rvadapter!!.allData[i].fileid
                        mCompeleteSize = 0L
                        offset = 0
                        if (rvadapter!!.allData[i].cayman_pretreat_mimetype != null)
                            mimetype = rvadapter!!.allData[i].cayman_pretreat_mimetype
                        exe = rvadapter!!.allData[i].ext
                        filetype = TaskService.DOWNLOAD
                        filesubtype = NORMALDOWNLOAD
                        taskstate = TaskService.NEWFILE
                        total = rvadapter!!.allData[i].filesize.toLong()
                        userid = Someutil.getUserID()
                        id = System.currentTimeMillis().toString()

                    }
                    AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)

                }
                rvadapter!!.setCheckBoxNoneSelect()
                updatecheckBox()
                this!!.toast("开始后台下载")
            }
            edit_rv_share -> {
            }
            edit_rv_delete -> {
                AlertDialog.Builder(this!!).run {
                    setMessage("30天内可在回收站内找回删除文件")
                    setPositiveButton("确认删除") { _, _ ->
                        rvadapter!!.apply {
                            getcheckboxArrary().forEach { i, _ ->
                                HttpUtil.instance.apiService().trushDo(Someutil.getToken(), allData[i].fileid
                                        , allData[i].objid, allData[i].createuid)
                                        .compose(RxSchedulers.compose())
                                        .subscribe({

                                            if (it.contains("200")) {
                                                this@SearchResultActivity!!.toast("删除成功！")
                                                rvadapter!!.setCheckBoxNoneSelect()
                                                updatecheckBox()
                                                Handler().postDelayed({
                                                    refreshData()
                                                }, 500)
                                            } else {
                                                this@SearchResultActivity!!.toast("删除失败！")
                                            }
                                            it.toString().LogD("delete file reuslt : ")
                                        }, {
                                            this@SearchResultActivity!!.toast("删除失败！")
                                        })
                            }
                        }
                    }
                    setNegativeButton("取消") { _, _ ->

                    }
                    show()

                }.apply {
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.red))
                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))


                }
            }
            edit_rv_dabaoxiazai -> {
            }
            edit_rv_copy -> {
            }
            edit_rv_move -> {
                edit_more_ll!!.visibility =
                        if (edit_more_ll!!.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE

            }
            edit_rv_detil -> {
                if (rvadapter!!.getcheckboxArrary().size() == 1)
                    this!!.startActivity(Intent(this, FileDetilActivity::class.java).putExtra("file", rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)]))
                updatecheckBox()
                Handler().postDelayed({
                    refreshData()
                }, 300)
            }


        }
    }

    private fun  refreshData(){
        rvadapter!!.setCheckBoxNoneSelect()
        rvadapter!!.notifyDataSetChanged()
        updatecheckBox()
        initdata(keyword)
    }


    private fun initdata(keyword: String) {


        HttpUtil.instance.apiService().filesimpleSearch(Someutil.getToken(), keyword, currentdirid, cpge, 40, Someutil.getUserID()
                , true, Someutil.getUserID())
                .compose(RxSchedulers.compose())
                .subscribe({

                    if (cpge == 1) {
                        rvadapter!!.clear()

                    }


                    if (it.reuslt.files.res.size < 40) {
                        rvadapter!!.stopMore()

                    }
                    rvadapter!!.addAll(it.reuslt.files.res)
                    rvadapter!!.notifyDataSetChanged()


                }, {
                    it.toString().LogD("error : ")

                })

        //(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        //(this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}
