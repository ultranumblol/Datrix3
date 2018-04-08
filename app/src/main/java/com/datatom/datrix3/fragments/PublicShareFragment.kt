package com.datatom.datrix3.fragments

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.datatom.datrix3.Adapter.openshareadapter
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.helpers.I
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class PublicShareFragment : BaseFragment() {

    private var rv: EasyRecyclerView? = null

    private var rvadapter: openshareadapter? = null

    override fun initview(view: View) {

        rv = view.find(I.rv_openshare)
        rvadapter = openshareadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter

        }

        var list = arrayListOf<Int>(12,12,123)



    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_publicshare;
    }


}