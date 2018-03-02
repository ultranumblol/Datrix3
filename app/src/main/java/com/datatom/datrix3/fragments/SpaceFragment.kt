package com.datatom.datrix3.fragments


import android.Manifest
import android.annotation.SuppressLint
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
import com.datatom.datrix3.Bean.SearchHis
import com.datatom.datrix3.Bean.SpaceType
import com.datatom.datrix3.R
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.*
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

import android.view.ViewGroup
import collections.forEach
import com.datatom.datrix3.Activities.PDFViewerActivity
import com.datatom.datrix3.Activities.PlayVideoActivity
import com.datatom.datrix3.Activities.ViewBigImageActivity
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.Someutil.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.datatom.datrix3.Util.Someutil.checkPermissionREAD_EXTERNAL_STORAGE
import io.github.tonnyl.charles.Charles
import io.github.tonnyl.charles.engine.impl.GlideEngine
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast
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

        var currentID = ""


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


        currentID = PERSONAL_SPACE_ID
        initData(currentID)

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

                "editState" -> {


                }

            }

        }

        rvadapter!!.apply {

            //setNoMore(R.layout.view_nomore)


            setMore(L.view_more, {
                Handler().postDelayed({
                    cpge++

                    rvadapter!!.setCheckBoxNoneSelect()
                    rvadapter!!.notifyDataSetChanged()

                    when (currentID) {

                        PERSONAL_SPACE_ID -> {
                            initData(PERSONAL_SPACE_ID)
                        }
                        PUBLIC_SPACE_ID -> {
                            initpublicData(PUBLIC_SPACE_ID)
                        }
                    }

                }, 1000)

            })
            setOnItemClickListener {

                rvadapter!!.allData[it].type.LogD("click  type : ")

                rvadapter!!.allData[it].cayman_pretreat_mimetype.LogD("click  cayman_pretreat_mimetype : ")

                rvadapter!!.allData[it].ext?.LogD("click  ext : ")

                when (rvadapter!!.allData[it].type) {
                    "0" -> {
                    }
                    "1" -> {
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


                                rvadapter!!.allData[it].toString().LogD("click : ")

                                context!!.startActivity(Intent(context, PlayVideoActivity::class.java)
                                        .putExtra("file", rvadapter!!.allData[it]))


                            }

                        // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                            "aideo"
                            -> {
                            }

                            "conf", "cpp", "htm", "html", "log", "sh", "txt", "xml"
                            -> {

                            }
                            "zip" -> {

                            }
                            "pdf" ->{

                                context!!.startActivity(Intent(context, PDFViewerActivity::class.java)
                                        .putExtra("file", rvadapter!!.allData[it]))


                            }
                        }
                    }

                }

            }

        }

    }

    private fun initData(listid: String) {


        HttpUtil.instance.apiService().persondir_listdirfiles(Someutil.getToken(), listid, Someutil.getUserID(),
                "type,createtime", "asc,desc",
                cpge, 40, -1, false, false
        )
                .compose(RxSchedulers.compose())
                .subscribe({

                    if (it.msg != null && it.msg.contains("获取token信息失败")) {

                        Someutil.updateToken()
                        initData(PERSONAL_SPACE_ID)
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

    private fun initpublicData(listid: String) {


        HttpUtil.instance.apiService().pubdir_listdirfiles(Someutil.getToken(), listid, Someutil.getUserID(),
                "type,createtime", "asc,desc",
                cpge, 40, -1, false, false
        )
                .compose(RxSchedulers.compose())
                .subscribe({

                    if (it.msg != null && it.msg.contains("获取token信息失败")) {

                        Someutil.updateToken()
                        initpublicData(PUBLIC_SPACE_ID)
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

        searchFiles()


    }

    private fun searchFiles() {


    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            I.space_zhezhao -> {
                hideCradview()


            }

            I.edit_rv_rename -> {

                var renametext = EditText(activity)

                renametext.apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    hint = " 文件名称"

                    textSize = 16f

                    setPadding(28, 14, 14, 14)


                }

                context.let {
                    AlertDialog.Builder(it!!).run {
                        setView(renametext)
                        setTitle("重命名")
                        setNegativeButton("取消") { _, _ ->

                        }
                        setPositiveButton("确认") { _, _ ->
                        }
                        show()
                    }.apply {
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
                    }
                }


            }
            I.edit_rv_xiazai -> {

                rvadapter!!.getcheckboxArrary().forEach { i, b ->

                    // i.toString().LogD("index : ")

                    var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + datalist[i].fileid + "&iswindows=0&optuser=admin"

                    url.LogD("url : ")

                    toast("开始后台下载")

                    HttpUtil.instance.downLoadApi().downloadFileWithFixedUrl(url)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.newThread())
                            .subscribe({


                                if (Someutil.writeResponseBodyToDisk(it, datalist[i].fileid)) {
                                    "下载成功".LogD()
                                    //  toast("下载成功")

                                } else {
                                    "下载失败".LogD()

                                }

                            }, {
                                it.toString().LogD("download error : ")
                            })


                }


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


            }
            I.edit_rv_share -> {
            }
            I.edit_rv_delete -> {

                AlertDialog.Builder(activity!!).run {
                    setMessage("30天内可在回收站内找回删除文件")
                    setPositiveButton("确认删除") { _, _ ->
                        //todo 删除文件
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


                var renametext = EditText(activity)
                renametext.apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    hint = " 请输入打包文件名"

                    textSize = 16f

                    setPadding(28, 14, 14, 14)


                }
                var editview2 = View.inflate(activity, R.layout.dialog_edittext_dabaoxiazai, null)


                context.let {
                    AlertDialog.Builder(it!!).run {
                        setView(editview2)
                        setTitle("打包下载")
                        setNegativeButton("取消") { _, _ ->

                        }
                        setPositiveButton("确认") { _, _ ->


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
            I.edit_rv_detil -> {

            }
            I.change_space_person -> {

                hideCradview()
                RxBus.get().post(SpaceType("个人空间"))
                currentID = PERSONAL_SPACE_ID
                initData(PERSONAL_SPACE_ID)


            }
            I.change_space_public -> {

                hideCradview()
                RxBus.get().post(SpaceType("公共空间"))
                currentID = PUBLIC_SPACE_ID

                initpublicData(PUBLIC_SPACE_ID)

            }
            I.change_space_team -> {

                hideCradview()
                RxBus.get().post(SpaceType("讨论组"))
                initData(TEAM_SPACE_ID)


            }
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

            I.space_newfile -> {
                var renametext = EditText(activity)

                renametext.apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    hint = " 文件夹名称"

                    textSize = 16f

                    setPadding(28, 24, 14, 14)

                    background = null


                }

                context.let {
                    AlertDialog.Builder(it!!).run {
                        setView(renametext)
                        setTitle("请输入文件夹名称")
                        setNegativeButton("取消") { _, _ ->

                        }
                        setPositiveButton("确认") { _, _ ->
                            HttpUtil.instance.apiService().createPersondir(Someutil.getToken(), renametext.text.toString(), Someutil.getUserID()
                                    , "", "", "", false)
                                    .compose(RxSchedulers.compose())
                                    .subscribe({
                                        it.toString().LogD("createfile reuslt : ")
                                        if (it.code == 200) {

                                            activity!!.toast("创建成功！")
                                            refreshData()
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
            I.space_upload -> {

                openchose()

            }
            I.space_pailie -> {

            }

        }


    }

    fun refreshData() {
        cpge = 1
        rvadapter!!.setCheckBoxNoneSelect()
        rvadapter!!.notifyDataSetChanged()

        when (currentID) {

            PERSONAL_SPACE_ID -> {
                initData(PERSONAL_SPACE_ID)
            }
            PUBLIC_SPACE_ID -> {
                initpublicData(PUBLIC_SPACE_ID)
            }
        }

    }


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