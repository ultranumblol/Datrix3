package com.datatom.datrix3.fragments

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.datatom.datrix3.Adapter.openshareadapter
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class ShareOpenedFragment : BaseFragment(){
    private var rv: EasyRecyclerView? = null

    private var rvadapter: openshareadapter? = null

    override fun initview(view: View) {

        rv = view.find(I.rv_openshare)
        rvadapter = openshareadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter

        }


        initdata()
        var list = arrayListOf<Int>(12,12,123)



    }

    private fun initdata() {

        HttpUtil.instance.apiService().linklist_create(Someutil.getToken(),Someutil.getUserID())
                .compose(RxSchedulers.compose())
                .subscribe({

                    it.reuslt.hits.toString().LogD("result : ")

                  rvadapter!!.addAll(it.reuslt.hits)
                },{
                    it.toString().LogD("error : ")
                })



    }


    override fun getLayoutitem(): Int {

        return R.layout.fragment_openedshare;
    }


}