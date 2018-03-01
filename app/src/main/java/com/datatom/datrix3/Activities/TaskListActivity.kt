package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.datatom.datrix3.Adapter.MyFragmentPagerAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.fragments.DownloadFragment
import com.datatom.datrix3.fragments.UploadFragment
import kotlinx.android.synthetic.main.activity_task_list.*
import org.jetbrains.anko.find


class TaskListActivity : BaseActivity() {


    private var vp : ViewPager? = null
    private var tab :TabLayout? = null

    override fun ActivityLayout(): Int {

        return R.layout.activity_task_list
    }

    override fun initView() {
        setToolbartitle("任务列表")

        vp = find(R.id.tasklist_vp)

        tab = find(R.id.tab_tasklist)

        var mtitles = arrayListOf("上传列表", "下载列表")

        var mlist = arrayListOf<Fragment>(UploadFragment(), DownloadFragment())

        vp!!.adapter = MyFragmentPagerAdapter(supportFragmentManager, mlist, mtitles)

        tab!!.setupWithViewPager(vp)

        tab!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> vp!!.currentItem = 0

                    1 -> vp!!.currentItem = 1

                }

            }


        })

        vp!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> vp!!.currentItem = position

                    1 -> vp!!.currentItem = position


                }

            }


        })

    }
}
