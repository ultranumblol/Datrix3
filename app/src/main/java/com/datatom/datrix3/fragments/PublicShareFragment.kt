package com.datatom.datrix3.fragments

import android.content.Intent
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.datatom.datrix3.Activities.SharedFilesActivity
import com.datatom.datrix3.Adapter.openshareadapter
import com.datatom.datrix3.Adapter.publicshareadapter
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class PublicShareFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var rv: EasyRecyclerView? = null

    private var rvadapter: publicshareadapter? = null

    override fun initview(view: View) {

        rv = view.find(I.rv_openshare)
        rvadapter = publicshareadapter(activity!!)
        rvadapter!!.apply {
            setOnItemClickListener {
                context.startActivity(Intent(activity, SharedFilesActivity::class.java).putExtra("files",allData[it]._source))

            }

        }
        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter
            setRefreshListener(this@PublicShareFragment)
            isNestedScrollingEnabled = true

        }

        initdata()
        RxBus.get().toFlowable(String::class.java).subscribe {
            when (it) {
                "refresh_publicshare" -> {
                    initdata()
                }
            }
        }
    }
    private fun initdata() {

        HttpUtil.instance.apiService().linklist_public(Someutil.getToken(), Someutil.getUserID())
                .compose(RxSchedulers.compose())
                .subscribe({
                    rvadapter!!.clear()

                    it.reuslt.hits.toString().LogD("linklist_public result : ")

                    rvadapter!!.addAll(it.reuslt.hits)
                }, {
                    it.toString().LogD("error : ")
                })
    }


    override fun getLayoutitem(): Int {

        return R.layout.fragment_publicshare;
    }
    override fun onRefresh() {

        Handler().postDelayed({
            initdata()
        }, 1000)

    }

}