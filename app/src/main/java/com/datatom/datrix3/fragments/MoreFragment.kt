package com.datatom.datrix3.fragments

import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView
import com.datatom.datrix3.Adapter.MoreFragmentadapter
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.Bean.MoreItems
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.I

import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

import org.jetbrains.anko.toast


/**
 * Created by wgz on 2018/1/23.
 */

class MoreFragment : BaseFragment(){

    var rvadapter : MoreFragmentadapter? = null

    var rv : EasyRecyclerView? = null

    var tvsetting  : TextView? = null

    var tvfankui  : TextView? = null

    private var nickname : TextView? = null

    companion object {

        val items = arrayListOf(MoreItems("回收站",R.drawable.ic_huishouzhan),
                MoreItems("扫一扫",R.drawable.ic_saoyisao),
                MoreItems("地图",R.drawable.ic_map),
                MoreItems("消息",R.drawable.ic_xiaoxi),
                MoreItems("内审",R.drawable.ic_neishen),
                MoreItems("编目",R.drawable.ic_bianmu),
                MoreItems("编目审核",R.drawable.ic_bianmushenhe),
                MoreItems("下载审核",R.drawable.ic_xiazaiashenhe),
                MoreItems("任务中心",R.drawable.ic_renwuzhongxin),
                MoreItems("统计",R.drawable.ic_tongji),
                MoreItems("闪电互传",R.drawable.ic_shandianhuchuan),
                MoreItems("高级搜索",R.drawable.ic_gaojisousuo)


        )




    }

    override fun initview(view: View) {


        rv = view.find(R.id.more_rv)

        tvfankui = view.find(R.id.more_fankui)

        tvsetting =view. find(I.more_setting)

        nickname = view.find(I.more_nicename)

        nickname!!.text = Someutil.getUserNickname()

        rvadapter = MoreFragmentadapter(activity!!)
        rv!!.apply {
            setLayoutManager(GridLayoutManager(activity,4))
            adapter = rvadapter
            //addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        }

        rvadapter!!.apply {
           addAll(items)
            setOnItemClickListener {
                activity!!.toast(" "+(it+1))
            }

        }


        tvsetting!!.apply {
            setOnClickListener{
                activity!!.toast("click!")

            }

        }
        tvfankui!!.apply {
            setOnClickListener{
                activity!!.toast("click!")

            }

        }





    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_more;
    }


}