package com.datatom.datrix3.fragments

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.datatom.datrix3.Adapter.SpaceAdapter
import com.datatom.datrix3.Base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.RxBus
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class SpaceFragment : BaseFragment() {

    private var rv: EasyRecyclerView? = null

    private var rvadapter: SpaceAdapter? = null



    override fun initview(view: View) {

        rv = view.find(I.space_rv)

        rvadapter = SpaceAdapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter

        }

        var data = arrayOf(1,2,1,23,1,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,22)

        rvadapter!!.apply {

            addAll(data)
        }

        RxBus.get().toFlowable(String::class.java).subscribe{
            if (it.equals("updatecheckbox"))
            {
               if (rvadapter!!.getcheckboxArrary().size()>0) {
                   RxBus.get().post("hidemainbar")

               }else{
                   RxBus.get().post("showmainbar")
               }

            }

        }

    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_space;
    }


}