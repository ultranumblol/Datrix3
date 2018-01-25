package com.datatom.datrix3.fragments

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.datatom.datrix3.Adapter.mycollectadapter
import com.datatom.datrix3.Base.BaseFragment
import com.datatom.datrix3.R
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class CollectionFragment : BaseFragment() {



    var rv: EasyRecyclerView? = null

    var rvadapter: mycollectadapter? = null

    override fun initview(view: View) {

        rv = view.find(R.id.rv_collect)

        rvadapter = mycollectadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter

        }

        var data = arrayListOf(1,2,3,4,5,6,7,8,9,0)


        rvadapter!!.addAll(data)


    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_collection;
    }


}