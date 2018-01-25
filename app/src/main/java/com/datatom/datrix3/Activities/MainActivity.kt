package com.datatom.datrix3.Activities

import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.datatom.datrix3.Adapter.MyFragmentPagerAdapter

import com.datatom.datrix3.R
import com.datatom.datrix3.fragments.CollectionFragment
import com.datatom.datrix3.fragments.MoreFragment
import com.datatom.datrix3.fragments.ShareFragment
import com.datatom.datrix3.fragments.SpaceFragment
import com.datatom.datrix3.helpers.I
import com.githang.statusbar.StatusBarCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {


    companion object {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))


        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.allselect, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_allselect) {


        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        when (main_vp.currentItem) {
            0 -> {
                menu!!.apply {
                    findItem(R.id.action_allselect).setVisible(true)
                    findItem(I.action_paixu).setVisible(true)
                }
            }
            1 -> {
                menu!!.apply {
                    findItem(R.id.action_allselect).setVisible(false)
                    findItem(I.action_paixu).setVisible(false)
                }
            }
            2 -> {
                menu!!.apply {
                    findItem(R.id.action_allselect).setVisible(true)
                    findItem(I.action_paixu).setVisible(false)
                }
            }


        }



        return super.onPrepareOptionsMenu(menu)
    }


    fun initView() {

        val mFragmentList = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        titles.add("空间")
        titles.add("分享")
        titles.add("收藏")
        titles.add("更多")

        mFragmentList.add(SpaceFragment())
        mFragmentList.add(ShareFragment())
        mFragmentList.add(CollectionFragment())
        mFragmentList.add(MoreFragment())

        val adapter = MyFragmentPagerAdapter(supportFragmentManager, mFragmentList, titles)

        main_vp.adapter = adapter

        main_vp.offscreenPageLimit = 4

        main_title.text = "个人空间"

        main_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                invalidateOptionsMenu()
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        main_title.text = "个人空间"
                        app_bar.visibility = View.VISIBLE
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.text = "切换"
                        toolbar_qiehuan.visibility = View.VISIBLE


                    }
                    1 -> {
                        main_title.text = "我的分享"
                        app_bar.visibility = View.VISIBLE
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.visibility = View.GONE
                    }
                    2 -> {
                        main_title.text = "我的收藏"
                        app_bar.visibility = View.VISIBLE
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.text = "全部"
                        toolbar_qiehuan.visibility = View.VISIBLE


                    }
                    3 -> {
                        main_title.text = ""
                        app_bar.visibility = View.GONE
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))


                    }


                }
            }

        })

        main_vp.currentItem = 0



        rv_space.setOnClickListener(this)
        rv_share.setOnClickListener(this)
        rv_collect.setOnClickListener(this)
        rv_more.setOnClickListener(this)


    }


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.rv_space -> {
                main_img_space.setImageResource(R.drawable.ic_space_click)
                main_img_collect.setImageResource(R.drawable.ic_collect)
                main_img_share.setImageResource(R.drawable.ic_share)
                main_img_more.setImageResource(R.drawable.ic_more)
                main_tv_space.setTextColor(resources.getColor(R.color.main_button_color))
                main_tv_collect.setTextColor(resources.getColor(R.color.gray))
                main_tv_more.setTextColor(resources.getColor(R.color.gray))
                main_tv_share.setTextColor(resources.getColor(R.color.gray))
                main_vp.currentItem = 0

            }
            R.id.rv_collect -> {
                main_img_space.setImageResource(R.drawable.ic_space)
                main_img_collect.setImageResource(R.drawable.ic_collect_click)
                main_img_share.setImageResource(R.drawable.ic_share)
                main_img_more.setImageResource(R.drawable.ic_more)
                main_tv_space.setTextColor(resources.getColor(R.color.gray))
                main_tv_collect.setTextColor(resources.getColor(R.color.main_button_color))
                main_tv_more.setTextColor(resources.getColor(R.color.gray))
                main_tv_share.setTextColor(resources.getColor(R.color.gray))
                main_vp.currentItem = 2

            }
            R.id.rv_share -> {
                main_img_space.setImageResource(R.drawable.ic_space)
                main_img_collect.setImageResource(R.drawable.ic_collect)
                main_img_share.setImageResource(R.drawable.ic_share_click)
                main_img_more.setImageResource(R.drawable.ic_more)
                main_tv_space.setTextColor(resources.getColor(R.color.gray))
                main_tv_collect.setTextColor(resources.getColor(R.color.gray))
                main_tv_more.setTextColor(resources.getColor(R.color.gray))
                main_tv_share.setTextColor(resources.getColor(R.color.main_button_color))
                main_vp.currentItem = 1

            }
            R.id.rv_more -> {
                main_img_space.setImageResource(R.drawable.ic_space)
                main_img_collect.setImageResource(R.drawable.ic_collect)
                main_img_share.setImageResource(R.drawable.ic_share)
                main_img_more.setImageResource(R.drawable.ic_more_click)
                main_tv_space.setTextColor(resources.getColor(R.color.gray))
                main_tv_collect.setTextColor(resources.getColor(R.color.gray))
                main_tv_more.setTextColor(resources.getColor(R.color.main_button_color))
                main_tv_share.setTextColor(resources.getColor(R.color.gray))
                main_vp.currentItem = 3

            }


        }

    }

}
