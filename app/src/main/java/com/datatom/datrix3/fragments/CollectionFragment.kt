package com.datatom.datrix3.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.datatom.datrix3.Activities.*
import com.datatom.datrix3.Adapter.mycollectadapter
import com.datatom.datrix3.Bean.ListIndex
import com.datatom.datrix3.Bean.SpacePageList
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.*
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class CollectionFragment : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    var rv: EasyRecyclerView? = null

    var rvadapter: mycollectadapter? = null

    private var zhezhao: FrameLayout? = null

    private var cardview: CardView? = null

    private var collect_all :RelativeLayout? = null
    private var collect_pic :RelativeLayout? = null
    private var collect_doc :RelativeLayout? = null
    private var collect_video :RelativeLayout? = null
    private var collect_music :RelativeLayout? = null
    private var collect_other :RelativeLayout? = null
    private var mimetype  = ""

    companion object {
        var lefttitle = ""
    }




    override fun initview(view: View) {

        rv = view.find(R.id.rv_collect)

        zhezhao = view.find(I.collect_zhezhao)
        cardview = view.find(I.collection_chose_cardview)
        collect_all = view.find(I.collect_chose_all)
        collect_pic = view.find(I.collect_chose_pic)
        collect_doc = view.find(I.collect_chose_doc)
        collect_video = view.find(I.collect_chose_video)
        collect_music = view.find(I.collect_chose_mic)
        collect_other = view.find(I.collect_chose_other)
        collect_all!!.setOnClickListener(this)
        collect_pic!!.setOnClickListener(this)
        collect_doc!!.setOnClickListener(this)
        collect_video!!.setOnClickListener(this)
        collect_music!!.setOnClickListener(this)
        collect_other!!.setOnClickListener(this)

        zhezhao!!.setOnClickListener(this)
        lefttitle = "全部"
        rvadapter = mycollectadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            setRefreshListener(this@CollectionFragment)
            adapter = rvadapter

        }
        rvadapter!!.apply {
            setOnItemClickListener {
                if (allData[it]._source.cayman_pretreat_mimetype != null)
                    allData[it]._source.cayman_pretreat_mimetype?.LogD("click  cayman_pretreat_mimetype : ")
                if (allData[it]._source.ext != null)
                    allData[it]._source.ext?.LogD("click  ext : ")
                when (rvadapter!!.allData[it]._source.type) {
                    "0" -> {
                        rvadapter!!.allData[it]._source.fileid

//                        var sp = SpacePageList(rvadapter!!.allData[it]._source.fileid, rvadapter!!.allData[it]._source.filename, 1, false, rvadapter!!.allData[it]._source.objid,rvadapter!!.allData[it]._source.parentid)
//
//                        pagelist.add(sp)
//                        pagelist.toString().LogD("after add pagelist : ")
//                        currentdirname = rvadapter!!.allData[it].filename
//                        currentdirid = rvadapter!!.allData[it].fileid
//                        showpageback()
//                        rvadapter!!.clear()
//                        initdata(keyword)


                    }
                    "1" -> {
                        if (rvadapter!!.allData[it]._source.cayman_pretreat_mimetype != null)
                            when (rvadapter!!.allData[it]._source.cayman_pretreat_mimetype) {
                                "image" -> {

                                    val imglist = arrayListOf<String>(rvadapter!!.allData[it]._source.fileid)

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

        initdata()

        // var data = arrayListOf(1,2,3,4,5,6,7,8,9,0)


        //rvadapter!!.addAll(data)

        RxBus.get().toFlowable(ListIndex::class.java).subscribe {
            when (it.type) {
                "collection" -> {

                    rvadapter!!.remove(it.index)
                    rvadapter!!.notifyDataSetChanged()
                }
            }

        }

        RxBus.get().toFlowable(String::class.java).subscribe {
            when (it) {

                "collect_chose" -> {
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

    private fun initdata() {

        HttpUtil.instance.apiService().collect_listmy(Someutil.getToken(), Someutil.getUserID(), mimetype, "")
                .compose(RxSchedulers.compose())
                .subscribe({
                    if (it.msg != null && it.msg.contains("获取token信息失败")) {

                        Someutil.updateToken()
                        initdata()
                    } else {
                        rvadapter!!.clear()

                        it.toString().LogD("${it.javaClass.name}  result ::")
                        rvadapter!!.addAll(it.reuslt.hits1.hits2s)
                    }


                },{
                    it.toString().LogD("error : ")
                })
    }

    override fun onClick(v: View?) {

        when (v) {

            zhezhao -> {
                hideCradview()
            }
            collect_all->{
                mimetype = ""
                lefttitle ="全部"
                RxBus.get().post("collection_changetype")
                initdata()
                hideCradview()
            }
            collect_pic->{
                mimetype = "image"
                lefttitle = "图片"
                RxBus.get().post("collection_changetype")
                initdata()
                hideCradview()
            }
            collect_music->{
                mimetype = "audio"
                lefttitle = "音乐"
                RxBus.get().post("collection_changetype")
                initdata()
                hideCradview()
            }
            collect_doc->{
                mimetype = "doc"
                lefttitle = "文档"
                RxBus.get().post("collection_changetype")
                initdata()
                hideCradview()
            }
            collect_video->{
                mimetype = "video"
                lefttitle = "视频"
                RxBus.get().post("collection_changetype")
                initdata()
                hideCradview()
            }
            collect_other->{
                mimetype = "unknow"
                lefttitle = "其他"
                RxBus.get().post("collection_changetype")
                initdata()
                hideCradview()
            }


        }


    }

    fun hideCradview() {

        RxBus.get().post("hidezhezhao")
        cardview!!.hide()
        zhezhao!!.hide()
    }


    override fun getLayoutitem(): Int {

        return R.layout.fragment_collection;
    }

    override fun onRefresh() {
        initdata()

    }
}