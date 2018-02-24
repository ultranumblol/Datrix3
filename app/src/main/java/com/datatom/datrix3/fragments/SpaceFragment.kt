package com.datatom.datrix3.fragments


import android.content.pm.ActivityInfo
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
import io.github.tonnyl.charles.Charles
import io.github.tonnyl.charles.engine.impl.GlideEngine


/**
 * Created by wgz on 2018/1/23.
 * 空间页面
 */

class SpaceFragment : BaseFragment(), View.OnClickListener {


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
    private var newfile : ImageView? = null
    private var uploadfile : ImageView? = null
    private var pailie : ImageView? = null



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
    private var zhezhao : FrameLayout? = null


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
        searchrv!!.setLayoutManager(LinearLayoutManager(activity!!))
        searchadapter = searchhisadapter(activity!!)
        searchrv!!.adapter = searchadapter

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

        }


        var data = arrayOf(1, 2, 1, 23, 1, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 22)

        rvadapter!!.apply {

            addAll(data)
        }

        editll!!.hide()
        editllmore!!.hide()

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

                    } else {
                        RxBus.get().post("showmainbar")
                        editll!!.hide()
                        editllmore!!.hide()
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

            }

        }


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

            I.space_zhezhao ->{
                hideCradview()




            }

            I.edit_rv_rename -> {

                var renametext = EditText(activity)

                renametext.apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)

                    hint = " 文件名称"

                    textSize = 16f

                    setPadding(28,14,14,14)





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

                context.let {
                    AlertDialog.Builder(it!!)
                            .run {
                                setMessage("你即将使用手机流量传输文件，继续传输将产生流量费用")
                                setNegativeButton("仅WI-FI传输"){_,_ ->


                                }
                                setPositiveButton("手机流量传输"){_,_ ->}
                                show()
                            }.apply {
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))

                    }


                }


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
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)

                    hint = " 请输入打包文件名"

                    textSize = 16f

                    setPadding(28,14,14,14)


                }
                var editview2 = View.inflate(activity,R.layout.dialog_edittext_dabaoxiazai,null)


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
            }
            I.change_space_public -> {

                hideCradview()
                RxBus.get().post(SpaceType("公共空间"))
            }
            I.change_space_team -> {

                hideCradview()
                RxBus.get().post(SpaceType("讨论组"))


            }
            I.search_layout -> {
                SearchViewUtils.handleToolBar(context!!, mCardViewSearch!!, mEtSearch!!)

                //Log.d("wgz", "his : " + database.SearchHisDao().querySearchHis().toString())


                searchadapter!!.apply {
                    clear()
                    searchhiss = database.SearchHisDao().querySearchHis().sortedByDescending { it.id }


                    addAll(searchhiss)

                }
                if (searchhiss!!.size > 0) {
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

            I.space_newfile ->{


            }
            I.space_upload ->{
                Charles.from(this)
                        .choose()
                        .maxSelectable(9)
                        .progressRate(true)
                        .theme(R.style.Charles)
                        .imageEngine(GlideEngine())
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .forResult(101)


            }
            I.space_pailie ->{

            }

        }


    }
    fun hideCradview(){
        RxBus.get().post("hidezhezhao")
        cardview!!.hide()
        zhezhao!!.hide()

    }
    fun showCradview(){

        cardview!!.Show()
        zhezhao!!.Show()

    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_space;
    }


}