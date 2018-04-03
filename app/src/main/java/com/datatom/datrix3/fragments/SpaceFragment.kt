package com.datatom.datrix3.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.datatom.datrix3.Adapter.SpaceAdapter
import com.datatom.datrix3.Adapter.searchhisadapter
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.*
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import collections.forEach
import com.datatom.datrix3.Activities.*
import com.datatom.datrix3.Bean.*
import com.datatom.datrix3.Service.TaskService.Companion.BEGDOWNLOAD
import com.datatom.datrix3.Service.TaskService.Companion.DOWNLOAD
import com.datatom.datrix3.Service.TaskService.Companion.DOWNLOADING
import com.datatom.datrix3.Service.TaskService.Companion.NEWFILE
import com.datatom.datrix3.Service.TaskService.Companion.NORMALDOWNLOAD
import com.datatom.datrix3.Service.TaskService.Companion.UPLOAD
import com.datatom.datrix3.Util.*
import com.datatom.datrix3.Util.Someutil.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.datatom.datrix3.Util.Someutil.checkPermissionREAD_EXTERNAL_STORAGE
import com.datatom.datrix3.app
import io.github.tonnyl.charles.Charles
import io.github.tonnyl.charles.engine.impl.GlideEngine
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


/**
 * Created by wgz on 2018/1/23.
 * 空间页面
 */

class SpaceFragment : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    companion object {

        val PERSONAL_SPACE_ID = "8A85A16BB60D88F0"
        val PUBLIC_SPACE_ID = "1E07037D38FA20BB"
        val TEAM_SPACE_ID = "5C59EBB7F9BDF00F"

        private var currentID = ""
        private var currentdir = ""
        private var currentParentObjid = ""


    }

    private var rv: EasyRecyclerView? = null

    private var rvadapter: SpaceAdapter? = null

    private var editll: LinearLayout? = null

    private var editllmore: LinearLayout? = null

    private var rename: RelativeLayout? = null
    private var download: RelativeLayout? = null
    private var share: RelativeLayout? = null
    private var delete: RelativeLayout? = null
    private var more: RelativeLayout? = null
    private var dabaoxiazai: RelativeLayout? = null
    private var copy: RelativeLayout? = null
    private var move: RelativeLayout? = null
    private var detil: RelativeLayout? = null
    private var newfile: ImageView? = null
    private var uploadfile: ImageView? = null
    private var pailie: ImageView? = null


    private var cardview: CardView? = null
    private var personspace: RelativeLayout? = null
    private var publicspace: RelativeLayout? = null

    private var teamspace: RelativeLayout? = null

    private var mCardViewSearch: CardView? = null

    private var searchll: LinearLayout? = null

    private var ivSearchBack: TextView? = null

    private var mEtSearch: EditText? = null

    private lateinit var database: AppDatabase

    private var searchhiss: List<SearchHis>? = null

    private var searchadapter: searchhisadapter? = null
    private var searchrv: RecyclerView? = null
    private var clear_his: TextView? = null
    private var zhezhao: FrameLayout? = null

    private var pagelist: ArrayList<SpacePageList> = arrayListOf()

    private var cpge = 1


    private lateinit var datalist: List<PersonalFilelistData.result2>

    override fun initview(view: View) {

        rv = view.find(I.space_rv)

        editll = view.find(I.edit_ll)

        editllmore = view.find(I.edit_more_ll)

        rvadapter = SpaceAdapter(activity!!)

        rename = view.find(I.edit_rv_rename)
        download = view.find(I.edit_rv_xiazai)
        share = view.find(I.edit_rv_share)
        delete = view.find(I.edit_rv_delete)
        more = view.find(I.edit_rv_more)
        dabaoxiazai = view.find(I.edit_rv_dabaoxiazai)
        copy = view.find(I.edit_rv_copy)
        move = view.find(I.edit_rv_move)
        detil = view.find(I.edit_rv_detil)
        zhezhao = view.find(I.space_zhezhao)
        mCardViewSearch = view.find(I.cardView_search)
        ivSearchBack = view.find(I.iv_search_back)
        mEtSearch = view.find(I.et_search)
        searchll = view.find(I.search_layout)
        searchrv = view.find(I.recycleview)
        clear_his = view.find(I.clear_his)
        pailie = view.find(I.space_pailie)
        uploadfile = view.find(I.space_upload)
        newfile = view.find(I.space_newfile)
        cardview = view.find(I.change_space_cardview)
        personspace = view.find(I.change_space_person)
        publicspace = view.find(I.change_space_public)
        teamspace = view.find(I.change_space_team)

        pailie!!.setOnClickListener(this)
        uploadfile!!.setOnClickListener(this)
        newfile!!.setOnClickListener(this)
        rename!!.setOnClickListener(this)
        download!!.setOnClickListener(this)
        share!!.setOnClickListener(this)
        delete!!.setOnClickListener(this)
        more!!.setOnClickListener(this)
        dabaoxiazai!!.setOnClickListener(this)
        copy!!.setOnClickListener(this)
        move!!.setOnClickListener(this)
        detil!!.setOnClickListener(this)
        zhezhao!!.setOnClickListener(this)

        personspace!!.setOnClickListener(this)
        publicspace!!.setOnClickListener(this)
        teamspace!!.setOnClickListener(this)
        searchll!!.setOnClickListener(this)
        clear_his!!.setOnClickListener(this)

        database = AppDatabase.getInstance(activity!!)



        mEtSearch!!.setOnEditorActionListener { _, i, _ ->

            if (i == EditorInfo.IME_ACTION_SEARCH) {

                //goSearch(mEtSearch.getText().toString())

                if (!mEtSearch!!.text.toString().isEmpty()) {
                    goSearch(mEtSearch!!.text.toString())

                    true
                }

            }

            false
        }
        searchrv!!.layoutManager = LinearLayoutManager(activity!!)
        searchadapter = searchhisadapter(activity!!)

        searchrv!!.apply {
            adapter = searchadapter

        }

        searchadapter!!.setOnItemClickListener {

            goSearch(searchadapter!!.getItem(it).searchstr)
            database.SearchHisDao().deleteHis(searchadapter!!.getItem(it))

        }

        ivSearchBack!!.setOnClickListener {
            SearchViewUtils.handleToolBar(context!!, mCardViewSearch!!, mEtSearch!!)
        }

        cardview!!.hide()

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter
            setRefreshListener(this@SpaceFragment)


        }

        pagelist = arrayListOf()
        pagelist.add(SpacePageList(PERSONAL_SPACE_ID, "个人空间", 1, true, ""))
        currentID = PERSONAL_SPACE_ID
        currentdir = PERSONAL_SPACE_ID
        initData(currentdir)

        editll!!.hide()
        editllmore!!.hide()


        //rxbus 控制
        RxBus.get().toFlowable(String::class.java).subscribe {

            when (it) {
                "spaceselectall" -> {

                    // Log.d("wgz", "getcheckboxArrary :" + rvadapter!!.getcheckboxArrary().size())
                    //Log.d("wgz", "adaptersize :" + rvadapter!!.count)

                    rvadapter!!.apply {
                        if (rvadapter!!.getcheckboxArrary().size() > 0 && rvadapter!!.getcheckboxArrary().size() == rvadapter!!.count) {
                            setCheckBoxNoneSelect()
                            notifyDataSetChanged()
                        } else {
                            setCheckBoxAllSelect()
                            notifyDataSetChanged()
                        }
                    }
                    RxBus.get().post("updatespacecheckbox")


                }
                "updatespacecheckbox" -> {
                    if (rvadapter!!.getcheckboxArrary().size() > 0) {
                        RxBus.get().post("hidemainbar")
                        editll!!.Show()
                        // rvadapter!!.showCheckBox()
                        rvadapter!!.notifyDataSetChanged()


                    } else {
                        RxBus.get().post("showmainbar")
                        // rvadapter!!.hideCheckBox()

                        editll!!.hide()
                        editllmore!!.hide()
                        rvadapter!!.notifyDataSetChanged()
                    }

                }
                "changespace" -> {

                    cardview!!.visibility =
                            if (cardview!!.visibility == View.GONE) View.VISIBLE
                            else View.GONE

                    if (cardview!!.visibility == View.GONE) {
                        RxBus.get().post("hidezhezhao")
                        zhezhao!!.hide()
                    } else {
                        RxBus.get().post("showzhezhao")
                        zhezhao!!.Show()
                    }

                }
                "hidecardview" -> {
                    hideCradview()

                }

                "pback" -> {
                    cpge = 1
                    // pagelist.toString().LogD("click goback  pagelist : ")

                    when (currentID) {
                        PERSONAL_SPACE_ID -> {
                            if (pagelist.size > 1)
                                pagelist.removeAt(pagelist.size - 1)
                            //pagelist.size.toString().LogD(" after remove list  : ")
                            currentdir = pagelist[pagelist.size - 1].fileid
                            currentParentObjid = pagelist[pagelist.size - 1].objid

                            rvadapter!!.clear()
                            initData(currentdir)
                            RxBus.get().post(pagelist[pagelist.size - 1])
                            // pagelist.size.toString().LogD("pagelist.size : ")


                        }
                        PUBLIC_SPACE_ID -> {
                            if (pagelist.size > 1)
                                pagelist.removeAt(pagelist.size - 1)
                            rvadapter!!.clear()
                            currentdir = pagelist[pagelist.size - 1].fileid
                            currentParentObjid = pagelist[pagelist.size - 1].objid

                            initpublicData(currentdir)
                            RxBus.get().post(pagelist[pagelist.size - 1])


                        }

                    }


                }

            }

        }

        //适配器适配数据
        rvadapter!!.apply {

            //setNoMore(R.layout.view_nomore)


            setMore(L.view_more, {
                Handler().postDelayed({
                    cpge++

                    rvadapter!!.setCheckBoxNoneSelect()
                    rvadapter!!.notifyDataSetChanged()

                    when (currentID) {

                        PERSONAL_SPACE_ID -> {
                            //todo 加载更多

                            initData(currentdir)
                        }
                        PUBLIC_SPACE_ID -> {
                            initpublicData(currentdir)
                        }
                    }

                }, 1000)

            })
            //设置适配器点击事件
            setOnItemClickListener {

                //                allData[it].type.LogD("click  type : ")
//                allData[it].parentid.LogD("parentid: ")
//                allData[it].objid.LogD("objid: ")
//                allData[it].fileid.LogD("fileid: ")
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

                            when (currentID) {
                                PERSONAL_SPACE_ID -> {

                                    var sp = SpacePageList(rvadapter!!.allData[it].fileid, rvadapter!!.allData[it].filename, 1, false, rvadapter!!.allData[it].objid)
                                    cpge = 1
                                    pagelist.add(sp)
                                    pagelist.toString().LogD("after add pagelist : ")

                                    currentdir = rvadapter!!.allData[it].fileid
                                    currentParentObjid = rvadapter!!.allData[it].objid
                                    rvadapter!!.clear()
                                    initData(currentdir)
                                    RxBus.get().post(sp)


                                }
                                PUBLIC_SPACE_ID -> {

                                    var sp = SpacePageList(rvadapter!!.allData[it].fileid, rvadapter!!.allData[it].filename, 2, false, rvadapter!!.allData[it].objid)
                                    pagelist.add(sp)
                                    cpge = 1
                                    currentdir = rvadapter!!.allData[it].fileid
                                    currentParentObjid = rvadapter!!.allData[it].objid
                                    rvadapter!!.clear()
                                    initpublicData(currentdir)
                                    RxBus.get().post(sp)
                                }
                            }
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

    }

    /**
     * 加载个人空间数据
     */
    private fun initData(listid: String) {


        HttpUtil.instance.apiService().persondir_listdirfiles(Someutil.getToken(), listid, Someutil.getUserID(),
                "type,createtime", "asc,desc",
                cpge, 40, -1, false, false
        )
                .compose(RxSchedulers.compose())
                .subscribe({

                    if (it.msg != null && it.msg.contains("获取token信息失败")) {

                        Someutil.updateToken()
                        initData(currentdir)
                    } else {

                        //it.toString().LogD("person : ")

                        if (cpge == 1) {
                            rvadapter!!.clear()

                        }

                        datalist = it.reuslt.result2
                        if (datalist.size < 40) {
                            rvadapter!!.stopMore()

                        }
                        rvadapter!!.addAll(datalist)
                        rvadapter!!.notifyDataSetChanged()
                        rvadapter!!.allData.toString().LogD(" all data : ")
                    }


                }, {
                    it.toString().LogD("error : ")
                    rvadapter!!.apply {
                        clear()
                        notifyDataSetChanged()

                    }
                })


    }

    /**
     * 加载公共空间数据
     */
    private fun initpublicData(listid: String) {


        HttpUtil.instance.apiService().pubdir_listdirfiles(Someutil.getToken(), listid, Someutil.getUserID(),
                "type,createtime", "asc,desc",
                cpge, 40, -1, false, false
        )
                .compose(RxSchedulers.compose())
                .subscribe({

                    if (it.msg != null && it.msg.contains("获取token信息失败")) {

                        Someutil.updateToken()
                        initpublicData(currentdir)
                    } else {
                        //it.toString().LogD("public : ")
                        datalist = it.reuslt.dir.result2
                        if (cpge == 1) {
                            rvadapter!!.clear()

                        }
                        if (datalist.size < 40) {
                            rvadapter!!.stopMore()

                        }

                        rvadapter!!.addAll(datalist)
                        rvadapter!!.notifyDataSetChanged()

                        rvadapter!!.allData.toString().LogD(" all data : ")
                    }


                }, {

                    rvadapter!!.apply {
                        clear()
                        notifyDataSetChanged()
                    }

                })


    }

    private fun goSearch(str: String) {


        SearchViewUtils.handleToolBar(context!!, mCardViewSearch!!, mEtSearch!!)

        database.SearchHisDao().insert(SearchHis(str, System.currentTimeMillis()))

        searchFiles(str)


    }

    //搜索目录下的文件
    private fun searchFiles(str: String) {
        //SearchViewUtils.handleToolBar(context!!, mCardViewSearch!!, mEtSearch!!)
        (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)

        context!!.startActivity(Intent(activity, SearchResultActivity::class.java).putExtra("keyword", str).putExtra("dirid", currentdir))

    }

    //所有控件点击事件
    override fun onClick(v: View?) {

        when (v!!.id) {

        //遮罩层隐藏
            I.space_zhezhao -> {
                hideCradview()


            }

        //重命名
            I.edit_rv_rename -> {
                var editview2 = View.inflate(activity, R.layout.dialog_edittext_dabaoxiazai, null)
                editview2.find<TextView>(I.dialog_edittext).hint = "请输入文件名称"

                if (rvadapter!!.getcheckboxArrary().size() == 1)
                    context.let {

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
                                                        activity!!.toast("重命名成功！")
                                                    rvadapter!!.setCheckBoxNoneSelect()
                                                    RxBus.get().post("updatespacecheckbox")
                                                    Handler().postDelayed({
                                                        refreshData()
                                                    }, 1000)
                                                }, {
                                                    it.toString().LogD("error : ")
                                                    activity!!.toast("重命名失败！")
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
                                                        activity!!.toast("重命名成功！")
                                                    rvadapter!!.setCheckBoxNoneSelect()
                                                    RxBus.get().post("updatespacecheckbox")
                                                    Handler().postDelayed({
                                                        refreshData()
                                                    }, 1000)
                                                }, {
                                                    it.toString().LogD("error : ")
                                                    activity!!.toast("重命名失败！")
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
            I.edit_rv_xiazai -> {

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
                        filetype = DOWNLOAD
                        filesubtype = NORMALDOWNLOAD
                        taskstate = NEWFILE
                        total = rvadapter!!.allData[i].filesize.toLong()
                        userid = Someutil.getUserID()
                        id = System.currentTimeMillis().toString()

                    }
                    AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)

                }

                //todo 添加网络类型判断
//                context.let {
//                    AlertDialog.Builder(it!!)
//                            .run {
//                                setMessage("你即将使用手机流量传输文件，继续传输将产生流量费用")
//                                setNegativeButton("仅WI-FI传输") { _, _ ->
//
//
//                                    //
//
//                                }
//                                setPositiveButton("手机流量传输") { _, _ -> }
//                                show()
//                            }.apply {
//                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
//                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
//
//                    }
//
//                }
                rvadapter!!.setCheckBoxNoneSelect()
                RxBus.get().post("updatespacecheckbox")
                context!!.toast("开始后台下载")


            }
            I.edit_rv_share -> {
            }
            I.edit_rv_delete -> {

                AlertDialog.Builder(activity!!).run {
                    setMessage("30天内可在回收站内找回删除文件")
                    setPositiveButton("确认删除") { _, _ ->
                        rvadapter!!.apply {
                            getcheckboxArrary().forEach { i, _ ->
                                HttpUtil.instance.apiService().trushDo(Someutil.getToken(), allData[i].fileid
                                        , allData[i].objid, allData[i].createuid)
                                        .compose(RxSchedulers.compose())
                                        .subscribe({

                                            if (it.contains("200")) {
                                                activity!!.toast("删除成功！")
                                                rvadapter!!.setCheckBoxNoneSelect()
                                                RxBus.get().post("updatespacecheckbox")
                                                Handler().postDelayed({
                                                    refreshData()
                                                }, 500)
                                            } else {
                                                activity!!.toast("删除失败！")
                                            }
                                            it.toString().LogD("delete file reuslt : ")
                                        }, {
                                            activity!!.toast("删除失败！")
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
            I.edit_rv_more -> {

                editllmore!!.visibility =
                        if (editllmore!!.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE


            }
            I.edit_rv_dabaoxiazai -> {

                var editview2 = View.inflate(activity, R.layout.dialog_edittext_dabaoxiazai, null)
                editview2.find<TextView>(I.dialog_edittext).hint = "请输入打包文件名"

                context.let {
                    AlertDialog.Builder(it!!).run {
                        setView(editview2)
                        setTitle("打包下载")
                        setNegativeButton("取消") { _, _ ->

                        }
                        setPositiveButton("确认") { _, _ ->

                            var fileidstr = StringBuilder()
                            var filetotla  = 0L

                            rvadapter!!.getcheckboxArrary().forEach { i, _ ->

                                fileidstr.append(rvadapter!!.allData[i].fileid)
                                fileidstr.append(",")
                                filetotla += rvadapter!!.allData[i].filesize.toLong()

//                                var taskfile = TaskFile()
//                                taskfile!!.apply {
//
//                                    filename = rvadapter!!.allData[i].filename
//                                    fileid = rvadapter!!.allData[i].fileid
//                                    mCompeleteSize = 0L
//                                    offset = 0
//                                    if (rvadapter!!.allData[i].cayman_pretreat_mimetype != null)
//                                        mimetype = rvadapter!!.allData[i].cayman_pretreat_mimetype
//                                    exe = rvadapter!!.allData[i].ext
//                                    filetype = DOWNLOAD
//                                    taskstate = NEWFILE
//                                    total = rvadapter!!.allData[i].filesize.toLong()
//                                    userid = Someutil.getUserID()
//                                    id = System.currentTimeMillis().toString()
//
//                                }
//                                AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)

                            }
                            fileidstr.deleteCharAt(fileidstr.length - 1)
                            var url = HttpUtil.BASEAPI_URL + "viewer/dcomp.php?fileidstr=" + fileidstr + "&iswindows=0&optuser=${Someutil.getUserID()}"

                            var taskfile = TaskFile()
                            taskfile!!.apply {
                                filename = editview2.find<TextView>(I.dialog_edittext).text.toString()+".tar"
                                fileid = System.currentTimeMillis().toString()
                                id = System.currentTimeMillis().toString()
                                taskstate = NEWFILE
                                filesubtype = BEGDOWNLOAD
                                mCompeleteSize = 0L
                                offset = 0
                                downloadurl = url
                                exe = "tar"
                                filetype = DOWNLOAD
                                userid = Someutil.getUserID()
                                total = filetotla
                            }
                            AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)
                            rvadapter!!.setCheckBoxNoneSelect()
                            RxBus.get().post("updatespacecheckbox")
                            context!!.toast("开始后台下载")


                        }
                        show()
                    }.apply {
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
                    }
                }

            }
            I.edit_rv_copy -> {

            }
            I.edit_rv_move -> {

            }
        //详情
            I.edit_rv_detil -> {
                if (rvadapter!!.getcheckboxArrary().size() == 1)
                    context!!.startActivity(Intent(activity, FileDetilActivity::class.java).putExtra("file", rvadapter!!.allData[rvadapter!!.getcheckboxArrary().keyAt(0)]))

                Handler().postDelayed({
                    refreshData()
                }, 300)

            }

        //切换到个人空间
            I.change_space_person -> {

                hideCradview()
                RxBus.get().post(SpaceType("个人空间"))
                currentID = PERSONAL_SPACE_ID
                currentdir = PERSONAL_SPACE_ID
                currentParentObjid = ""
                cpge = 1
                pagelist = arrayListOf()
                pagelist.add(SpacePageList(PERSONAL_SPACE_ID, "个人空间", 1, true, ""))
                rvadapter!!.clear()
                initData(currentdir)


            }
        //切换到公共空间
            I.change_space_public -> {

                hideCradview()
                RxBus.get().post(SpaceType("公共空间"))
                currentID = PUBLIC_SPACE_ID
                currentdir = PUBLIC_SPACE_ID
                currentParentObjid = ""
                cpge = 1
                pagelist = arrayListOf()
                pagelist.add(SpacePageList(PUBLIC_SPACE_ID, "公共空间", 2, true, ""))
                rvadapter!!.clear()
                initpublicData(currentdir)

            }
        //切换到讨论组
            I.change_space_team -> {
                cpge = 1
                hideCradview()
                currentID = TEAM_SPACE_ID
                currentdir = TEAM_SPACE_ID
                currentParentObjid = ""
                RxBus.get().post(SpaceType("讨论组"))
                rvadapter!!.clear()
                initData(TEAM_SPACE_ID)


            }
        //点击搜索框
            I.search_layout -> {
                SearchViewUtils.handleToolBar(context!!, mCardViewSearch!!, mEtSearch!!)
                //Log.d("wgz", "his : " + database.SearchHisDao().querySearchHis().toString())
                searchadapter!!.apply {
                    clear()
                    searchhiss = database.SearchHisDao().querySearchHis().sortedByDescending { it.id }
                    addAll(searchhiss)

                }
                if (searchhiss!!.isNotEmpty()) {
                    clear_his!!.Show()
                } else clear_his!!.hide()
            }
        //清除搜索历史记录
            I.clear_his -> {

                searchhiss!!.forEach {
                    database.SearchHisDao().deleteHis(it)
                }

                searchadapter!!.apply {
                    clear()
                    notifyDataSetChanged()
                    clear_his!!.hide()
                }
            }
        //创建新文件夹
            I.space_newfile -> {

                var editview2 = View.inflate(activity, R.layout.dialog_edittext_dabaoxiazai, null)
                editview2.find<TextView>(I.dialog_edittext).hint = "请输入文件夹名称"

                context.let {
                    AlertDialog.Builder(it!!).run {
                        setView(editview2)
                        setTitle("请输入文件夹名称")
                        setNegativeButton("取消") { _, _ ->

                        }
                        setPositiveButton("确认") { _, _ ->
                            HttpUtil.instance.apiService().createPersondir(Someutil.getToken(), editview2.find<TextView>(I.dialog_edittext).text.toString(), Someutil.getUserID()
                                    , Someutil.getUserID(), currentdir, currentParentObjid, false)
                                    .compose(RxSchedulers.compose())
                                    .subscribe({
                                        it.toString().LogD("createfile reuslt : ")
                                        if (it.code == 200) {

                                            activity!!.toast("创建成功！")

                                            Handler().postDelayed({
                                                refreshData()
                                            }, 1000)
                                        } else {
                                            activity!!.toast("创建失败！")

                                        }


                                    }, {
                                        it.toString().LogD("httperror : ")
                                        activity!!.toast("创建失败！")

                                    })
                        }
                        show()
                    }.apply {
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
                    }
                }


            }
        //上传文件 选择文件
            I.space_upload -> {

                openchose()

            }
            I.space_pailie -> {

            }

        }


    }

    //刷新数据
    fun refreshData() {

        //currentdir.LogD("currentdir : ")
        cpge = 1
        rvadapter!!.setCheckBoxNoneSelect()
        rvadapter!!.notifyDataSetChanged()
        RxBus.get().post("updatespacecheckbox")
        when (currentID) {

            PERSONAL_SPACE_ID -> {
                initData(currentdir)
            }
            PUBLIC_SPACE_ID -> {
                initpublicData(currentdir)
            }
        }

    }

    //打开文件选择器
    fun openchose() {

        if (checkPermissionREAD_EXTERNAL_STORAGE(this.context!!)) {
            Charles.from(this)
                    .choose()
                    .maxSelectable(9)
                    .progressRate(true)
                    .theme(R.style.Charles)
                    .imageEngine(GlideEngine())
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .forResult(101)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            // val uris = Charles.obtainResult(data)
            val paths = Charles.obtainPathResult(data)

            paths!!.forEach {
                //var taskfile = TaskFile(filePath ="/storage/emulated/0/DCIM/Camera/testtxt.txt")


                var taskfile = TaskFile(filePath = it)
                taskfile!!.apply {

                    mCompeleteSize = 0L
                    offset = 0
                    filetype = UPLOAD
                    taskstate = NEWFILE
                    parentobj = currentParentObjid
                    dirid = currentdir
                    userid = Someutil.getUserID()
                    id = System.currentTimeMillis().toString()

                }
                AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)

            }

            paths.toString().LogD(" paths : ")


        }
    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                Charles.from(this)
                        .choose()
                        .maxSelectable(9)
                        .progressRate(true)
                        .theme(R.style.Charles)
                        .imageEngine(GlideEngine())
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .forResult(101)
            } else {
                "permission error : ".LogD()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults)
        }
    }


    fun hideCradview() {
        RxBus.get().post("hidezhezhao")
        cardview!!.hide()
        zhezhao!!.hide()

    }

    fun showCradview() {

        cardview!!.Show()
        zhezhao!!.Show()

    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_space;
    }


    override fun onRefresh() {

        Handler().postDelayed({
            refreshData()
        }, 1000)


    }

}