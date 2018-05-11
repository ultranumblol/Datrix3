package com.datatom.datrix3.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Environment
import android.os.Looper
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView
import com.datatom.datrix3.Activities.*
import com.datatom.datrix3.Adapter.MoreFragmentadapter
import com.datatom.datrix3.Base.GlideApp
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.Bean.MoreItems
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.*
import com.datatom.datrix3.Util.Someutil.getCacheSize
import com.datatom.datrix3.app
import com.datatom.datrix3.base.AppConstant
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus

import com.jude.easyrecyclerview.EasyRecyclerView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find

import org.jetbrains.anko.toast
import kotlin.concurrent.thread


/**
 * Created by wgz on 2018/1/23.
 */

class MoreFragment : BaseFragment(), View.OnClickListener {


    override fun onClick(v: View?) {

        when (v) {

            logoutbutton -> {
                startActivity(Intent(activity, LoginActivity::class.java))

                SPUtils.remove(app.mapp, AppConstant.AUTO_LOGIN)
                GlideApp.get(activity!!).clearMemory()
                activity!!.finish()
                Runnable {
                    thread {
                        AppDatabase.getInstance(app.mapp).OfficefileDao().nukeTable()
                        AppDatabase.getInstance(app.mapp).SearchHisDao().nukeTable()

                        GlideApp.get(activity!!).clearDiskCache()
                    }
                }
            }
        }

    }

    private var rvadapter: MoreFragmentadapter? = null

    private var rv: EasyRecyclerView? = null

    private var tvsetting: TextView? = null

    private var tvfankui: TextView? = null

    private var quota: TextView? = null

    private var nickname: TextView? = null

    private var clearcache: TextView? = null

    private var logoutbutton: TextView? = null

    companion object {

        val items = arrayListOf(MoreItems("回收站", R.drawable.ic_huishouzhan),
                MoreItems("扫一扫", R.drawable.ic_saoyisao),
                MoreItems("地图", R.drawable.ic_map),
                MoreItems("消息", R.drawable.ic_xiaoxi),
                MoreItems("内审", R.drawable.ic_neishen),
                MoreItems("编目", R.drawable.ic_bianmu),
                MoreItems("编目审核", R.drawable.ic_bianmushenhe),
                MoreItems("下载审核", R.drawable.ic_xiazaiashenhe),
                MoreItems("任务中心", R.drawable.ic_renwuzhongxin),
                MoreItems("统计", R.drawable.ic_tongji),
                MoreItems("闪电互传", R.drawable.ic_shandianhuchuan),
                MoreItems("高级搜索", R.drawable.ic_gaojisousuo)


        )


    }

    override fun initview(view: View) {


        rv = view.find(R.id.more_rv)

        tvfankui = view.find(R.id.more_fankui)

        tvsetting = view.find(I.more_setting)

        nickname = view.find(I.more_nicename)

        clearcache = view.find(I.more_clearcache)

        logoutbutton = view.find(I.logout_button)

        quota = view.find(I.tv_quota)

        nickname!!.text = Someutil.getUserNickname()

        logoutbutton!!.setOnClickListener(this)

        rvadapter = MoreFragmentadapter(activity!!)
        rv!!.apply {
            setLayoutManager(GridLayoutManager(activity, 4))
            adapter = rvadapter
            //addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        }

        rvadapter!!.apply {
            addAll(items)
            setOnItemClickListener {

                when (allData[it].name) {
                    "回收站" -> {
                        activity!!.startActivity(Intent(activity, TrashActivity::class.java))
                    }
                    "扫一扫" -> {
                        if (Someutil.checkPermissionCAMERA(activity!!))
                            activity!!.startActivity(Intent(activity, SaomaActivity::class.java))
                        //activity!!.startActivity(Intent(activity,FileCollectWebViewActivity::class.java))


                    }
                    "地图" -> {


                    }

                }
            }
        }


        tvsetting!!.apply {
            setOnClickListener {
                activity!!.startActivity(Intent(activity!!,SettingsActivity::class.java))


            }

        }
        tvfankui!!.apply {
            setOnClickListener {
                activity!!.toast("click!")

            }

        }
        clearcache!!.apply {
            setOnClickListener {
                getCacheSize().LogD(" huancun :  ")
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("清理缓存")
                        .setMessage("缓存大小：" + getCacheSize() + "是否清除?")
                        .setPositiveButton("确认") { _, _ ->

                            Observable.create(ObservableOnSubscribe<Int> { e ->
                                e.onNext(1)
                                e.onComplete()
                            }).subscribeOn(Schedulers.io()).
                                    observeOn(Schedulers.io()).
                                    subscribe({
                                        Looper.prepare()
                                        GlideApp.get(app.mapp).clearDiskCache()
                                        //getFolderSize(File("${app.mapp.cacheDir}/IMG_CACHE")) + getFolderSize(File("${Environment.getExternalStorageDirectory()}/officetemp"))+getFolderSize(File("${Environment.getExternalStorageDirectory()}/datrixdownloadTemp"))
                                        Someutil.delFolder("${Environment.getExternalStorageDirectory()}/officetemp")
                                        Someutil.delFolder("${Environment.getExternalStorageDirectory()}/datrixdownloadTemp")
                                    }, {
                                        it.toString().LogD("clear cache error :")
                                    }, {

                                    }
                                    )
                            handler.postDelayed({
                                activity!!.toast("清理完成")
                            }, 1500)
                        }.setNegativeButton("取消", null).show()
                GlideApp.get(app.mapp).clearMemory()

            }
        }

        initData()

    }

    private fun initData() {
        HttpUtil.instance.apiService().userQuota(Someutil.getToken(), Someutil.getUserID())
                .compose(RxSchedulers.compose())
                .subscribe({
                    // quota!!.text = "容量（${SizeUtils.getSize(it.res.used)}/${SizeUtils.getSize(it.res.quota)} ${((it.res.used.toLong()/it.res.quota.toLong())*100)}%）"
                    if (it.res.used != null && it.res.quota != null)
                        quota!!.text = "容量（${SizeUtils.getSize(it.res.used)}/${SizeUtils.getSize(it.res.quota)}）"
                    else
                        quota!!.text = "容量（-/-）"

                }, {
                    it.toString().LogD("quota error : ")
                    quota!!.text = "容量（-/-）"
                    Thread.sleep(2000)
                    // initData()
                })


    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_more;
    }


}