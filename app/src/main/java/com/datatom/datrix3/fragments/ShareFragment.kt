package com.datatom.datrix3.fragments

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.datatom.datrix3.Adapter.MyFragmentPagerAdapter
import com.datatom.datrix3.Base.BaseFragment

import com.datatom.datrix3.R
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find


/**
 * Created by wgz on 2018/1/23.
 */

class ShareFragment : BaseFragment() {

    var sharevp: ViewPager? = null

    var tabshare: TabLayout? = null

    companion object {

        var mtitles = arrayListOf("开启的分享", "公共分享", "给我的分享")


    }


    override fun initview(view: View) {

        var mlist = arrayListOf<Fragment>(ShareOpenedFragment(), PublicShareFragment(), ShareToMeFragment())

        sharevp = view.find(R.id.share_vp)

        sharevp!!.adapter = MyFragmentPagerAdapter(childFragmentManager, mlist, mtitles)

        tabshare = view.find(R.id.tab_share)

        tabshare!!.apply {
            setupWithViewPager(sharevp)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabSelected(tab: TabLayout.Tab?) {

                    when (tab?.position) {
                        0 -> sharevp!!.currentItem = 0

                        1 -> sharevp!!.currentItem = 1

                        2 -> sharevp!!.currentItem = 2

                    }

                }


            })
        }

        sharevp!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

                when (position) {
                    0 -> sharevp!!.currentItem = 0

                    1 -> sharevp!!.currentItem = 1

                    2 -> sharevp!!.currentItem = 2

                }
            }


        })


    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_share
    }


}