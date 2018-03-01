package com.datatom.datrix3.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.datatom.datrix3.Adapter.MyFragmentPagerAdapter
import com.datatom.datrix3.Bean.SpaceType

import com.datatom.datrix3.R
import com.datatom.datrix3.fragments.CollectionFragment
import com.datatom.datrix3.fragments.MoreFragment
import com.datatom.datrix3.fragments.ShareFragment
import com.datatom.datrix3.fragments.SpaceFragment
import com.datatom.datrix3.helpers.*
import com.githang.statusbar.StatusBarCompat
import io.github.tonnyl.whatsnew.item.item
import io.github.tonnyl.whatsnew.item.whatsNew
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {


    var collection_choseStr : String = "全部"

    var titlename : String = ""

    companion object {
        val titles = arrayListOf("空间","分享","收藏","更多")

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

            when(main_vp.currentItem){
                0 ->
                {
                    RxBus.get().post("spaceselectall")
                }
                2 ->{
                    RxBus.get().post("collectselectall")
                }
            }



        }
        if (id == R.id.action_paixu){

            this.startActivity(Intent(this,TaskListActivity::class.java))


        }


        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        when (main_vp.currentItem) {
            0 -> {
                menu!!.apply {
                    findItem(R.id.action_allselect).isVisible = true
                    findItem(I.action_paixu).isVisible = true
                }
            }
            1 -> {
                menu!!.apply {
                    findItem(R.id.action_allselect).isVisible = false
                    findItem(I.action_paixu).isVisible = false
                }
            }
            2 -> {
                menu!!.apply {
                    findItem(R.id.action_allselect).isVisible = true
                    findItem(I.action_paixu).isVisible = false
                }
            }


        }



        return super.onPrepareOptionsMenu(menu)
    }


    fun initView() {

        val mFragmentList = ArrayList<Fragment>()


        mFragmentList.add(SpaceFragment())
        mFragmentList.add(ShareFragment())
        mFragmentList.add(CollectionFragment())
        mFragmentList.add(MoreFragment())

        val adapter = MyFragmentPagerAdapter(supportFragmentManager, mFragmentList, titles)

        main_vp.adapter = adapter

        main_vp.offscreenPageLimit = 4

        titlename = "个人空间"
        main_title.text = titlename


        main_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                invalidateOptionsMenu()
            }

            override fun onPageSelected(position: Int) {



                when (position) {
                    0 -> {
                      //  main_title.text = "个人空间"
                        main_title.text = titlename
                        app_bar.Show()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.text = "切换"
                        toolbar_qiehuan.Show()


                    }
                    1 -> {
                        main_title.text = "我的分享"
                        app_bar.Show()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.hide()
                    }
                    2 -> {
                        main_title.text = "我的收藏"
                        app_bar.Show()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.text = collection_choseStr
                        toolbar_qiehuan.Show()


                    }
                    3 -> {
                        main_title.text = ""
                        app_bar.hide()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))


                    }


                }
            }

        })

        main_vp.currentItem = 0
        main_bar.Show()
        main_bar_line.Show()
        zhezhao.hide()

        rv_space.setOnClickListener(this)
        rv_share.setOnClickListener(this)
        rv_collect.setOnClickListener(this)
        rv_more.setOnClickListener(this)
        toolbar_qiehuan.setOnClickListener(this)
        zhezhao.setOnClickListener(this)

        RxBus.get().toFlowable(String::class.java).subscribe {

            when(it){
                "hidemainbar" ->{
                    main_bar.hide()
                    main_bar_line.hide()
                }
                "showmainbar" ->{
                    main_bar.Show()
                    main_bar_line.Show()
                }
                "hidezhezhao" -> {
                    zhezhao.hide()

                }
                "showzhezhao" ->{
                    zhezhao.Show()
                }




          }

        }

        RxBus.get().toFlowable(SpaceType::class.java).subscribe{

            titlename = it.spacename
            main_title.text = it.spacename
        }


    }


    fun showAlertdialog(context : Context){




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
            I.toolbar_qiehuan ->{


                // code=3e0141a14b2c9540781d67c95b99e2d1,6,5,9&key=d8d5f823f3c625feeed6c3641bc7653f
//                var code  = "3e0141a14b2c9540781d67c95b99e2d1,6,5,9"
//
//                code.run {
//
//                    var strs = this.split(",")
//
//                    strs.toString().LogD(" str[] : ")
//
//                    var index =  strs[1].toInt() + strs[3].toInt()
//                    var length = strs[2].toInt()
//
//
//
//                    this.substring(index ,(index + length)).LogD("  ")
//
//
//                }

                if (main_bar.visibility != View.GONE){
                    when(main_vp.currentItem){
                        0 ->{
                            RxBus.get().post("changespace")

                        }

                        2 ->{

                            RxBus.get().post("collect_chose")

                        }



                    }
                }



//                val whatsnew = whatsNew {
//                    item {
//                        title = "Nice Icons"
//                        content = "Completely customize colors, texts and icons."
//                        imageRes = R.drawable.ic_gaojisousuo
//                    }
//                    item {
//                        title = "Such Easy"
//                        content = "Setting this up only takes 2 lines of code, impressive you say?"
//                        imageRes = R.drawable.ic_gaojisousuo
//                    }
//                }
//                whatsnew.presentAutomatically(this)


            }

            I.zhezhao ->{
                zhezhao.hide()
                RxBus.get().post("hidecardview")


            }



        }

    }

}
