package com.datatom.datrix3.Activities


import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.datatom.datrix3.Adapter.MyFragmentPagerAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.fragments.DownloadFragment
import com.datatom.datrix3.fragments.UploadFragment
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus
import org.jetbrains.anko.find
import q.rorbin.badgeview.QBadgeView
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {


    private var vp: ViewPager? = null
    private var tab: TabLayout? = null

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

        QBadgeView(this).bindTarget(tab_left)
                .setBadgeGravity(Gravity.CENTER or Gravity.END)
                .setBadgeTextSize(12f, true)
                .setGravityOffset(3f,10f,true)
                .setBadgePadding(4f, true).badgeNumber = 5

        QBadgeView(this).bindTarget(tab_right)
                .setBadgeGravity(Gravity.CENTER or Gravity.END)
                .setBadgeTextSize(12f, true)

                .setBadgePadding(4f, true).badgeNumber = 5
//
//        QBadgeView(this).bindTarget(tab)
//                .setBadgeGravity(Gravity.CENTER)
//                .setBadgeTextSize(12f, true)
//                .setGravityOffset(-20f,0f,true)
//                .setBadgePadding(4f, true).badgeNumber = 5


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.clear_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_clearlist) {

            when (vp!!.currentItem) {
                0 -> {
                    AlertDialog.Builder(this)
                            .setMessage("是否删除所有上传任务？")
                            .setPositiveButton("确定", { _, _ ->
                                RxBus.get().post("clearuploadlist")
                            })
                            .setNegativeButton("取消", { _, _ -> })
                            .show()

                }
                1 -> {

                    AlertDialog.Builder(this)
                            .setMessage("是否删除所有下载任务？")
                            .setPositiveButton("确定", { _, _ ->
                                RxBus.get().post("cleardownloadlist")
                            })
                            .setNegativeButton("取消", { _, _ -> })
                            .show()

                }
                else -> {

                    "??".LogD()
                }
            }


        }


        return super.onOptionsItemSelected(item)
    }
}
