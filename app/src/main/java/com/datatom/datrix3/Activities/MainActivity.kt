package com.datatom.datrix3.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.datatom.datrix3.Adapter.MyFragmentPagerAdapter
import com.datatom.datrix3.Bean.SpacePageList
import com.datatom.datrix3.Bean.SpaceType
import com.datatom.datrix3.Bean.TaskFile

import com.datatom.datrix3.R
import com.datatom.datrix3.Service.TaskService
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.fragments.CollectionFragment
import com.datatom.datrix3.fragments.MoreFragment
import com.datatom.datrix3.fragments.ShareFragment
import com.datatom.datrix3.fragments.SpaceFragment
import com.datatom.datrix3.helpers.*
import com.githang.statusbar.StatusBarCompat
import io.github.tonnyl.charles.Charles
import io.github.tonnyl.charles.engine.impl.GlideEngine
import io.github.tonnyl.whatsnew.item.item
import io.github.tonnyl.whatsnew.item.whatsNew
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {


    var collection_choseStr: String = "全部"

    var titlename: String = ""

    private var currentspacetype = 1
    private var showselectall = false


    companion object {
        val titles = arrayListOf("空间", "分享", "收藏", "更多")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        //setToolBar()
        initView()
        //启动消息推送服务
        startService(Intent(this, TaskService::class.java))
        Someutil.checkPermissionREAD_EXTERNAL_STORAGE(this)



        if (intent.getBooleanExtra("ifupdate", false)) {
            Someutil.updateToken()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.allselect, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_allselect) {

            when (main_vp.currentItem) {
                0 -> {
                    RxBus.get().post("spaceselectall")
                }
                2 -> {
                    RxBus.get().post("collectselectall")
                }
            }


        }
        if (id == R.id.action_paixu) {

            //this.startActivity(Intent(this, TaskListActivity::class.java))
            openchose()

        }


        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        when (main_vp.currentItem) {
            0 -> {
                menu!!.apply {
                    when(showselectall){
                        true ->{
                            findItem(R.id.action_allselect).isVisible = true
                            findItem(I.action_paixu).isVisible = true
                        }
                        false ->{
                            findItem(R.id.action_allselect).isVisible = false
                            findItem(I.action_paixu).isVisible = true
                        }
                    }

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
                        img_page_back.hide()




                    }
                    1 -> {
                        main_title.text = "我的分享"
                        app_bar.Show()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.hide()
                        img_page_back.hide()
                    }
                    2 -> {
                        main_title.text = "我的收藏"
                        app_bar.Show()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))
                        toolbar_qiehuan.text = collection_choseStr
                        toolbar_qiehuan.Show()
                        img_page_back.hide()


                    }
                    3 -> {
                        main_title.text = ""
                        app_bar.hide()
                        img_page_back.hide()
                        StatusBarCompat.setStatusBarColor(this@MainActivity, resources.getColor(R.color.colorPrimary))


                    }


                }
            }

        })

        main_vp.currentItem = 0
        main_bar.Show()
        main_bar_line.Show()
        zhezhao.hide()
        img_page_back.hide()

        rv_space.setOnClickListener(this)
        rv_share.setOnClickListener(this)
        rv_collect.setOnClickListener(this)
        rv_more.setOnClickListener(this)
        toolbar_qiehuan.setOnClickListener(this)
        zhezhao.setOnClickListener(this)
        img_page_back.setOnClickListener(this)

        RxBus.get().toFlowable(String::class.java).subscribe {

            when (it) {
                "hidemainbar" -> {
                    main_bar.hide()
                    main_bar_line.hide()
                    toolbar_qiehuan.text = "取消"
                    showselectall = true
                    invalidateOptionsMenu()


                }
                "showmainbar" -> {
                    main_bar.Show()
                    main_bar_line.Show()
                    toolbar_qiehuan.text = "切换"
                    showselectall = false
                    invalidateOptionsMenu()
                }
                "hidezhezhao" -> {
                    zhezhao.hide()

                }
                "showzhezhao" -> {
                    zhezhao.Show()
                }
                "updatemenu" ->{
                    invalidateOptionsMenu()
                }

            }

        }

        RxBus.get().toFlowable(SpaceType::class.java).subscribe {

            titlename = it.spacename
            main_title.text = it.spacename
        }
        RxBus.get().toFlowable(SpacePageList::class.java).subscribe {
            titlename = it.filename

            //it.filename.toString().LogD("filename : ")
            main_title.text = titlename
            img_page_back.Show()
            toolbar_qiehuan.hide()
            currentspacetype = it.spacetype

            if (it.ifroot) {
                toolbar_qiehuan.Show()
                img_page_back.hide()
                when (currentspacetype) {
                    1 -> {
                        titlename = "个人空间"
                        main_title.text = titlename

                    }
                    2 -> {
                        titlename = "公共空间"
                        main_title.text = titlename
                    }

                }

            }
        }


    }
    //打开文件选择器
    fun openchose() {

        HttpUtil.instance.apiService().checkuploadbyfileid(Someutil.getToken(),SpaceFragment.getcurrentdir(),SpaceFragment.getcurrentParentID(),Someutil.getUserID())
                .compose(RxSchedulers.compose())
                .subscribe {
                    if (it.reuslt.hasacl.isNotEmpty()){
                        if (Someutil.checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                            Charles.from(this)
                                    .choose()
                                    .maxSelectable(9)
                                    .progressRate(true)
                                    .theme(R.style.Charles)
                                    .imageEngine(GlideEngine())
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .forResult(101)

                        }
                    }
                    else{
                        this.toast("没有上传权限！")
                    }
                }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            // val uris = Charles.obtainResult(data)
            val paths = Charles.obtainPathResult(data)

            paths!!.forEach {
                //var taskfile = TaskFile(filePath ="/storage/emulated/0/DCIM/Camera/testtxt.txt")


                var taskfile = TaskFile(filePath = it)
                taskfile!!.apply {

                    mCompeleteSize = 0L
                    offset = 0
                    filetype = TaskService.UPLOAD
                    taskstate = TaskService.NEWFILE
                    parentobj =SpaceFragment.getcurrentParentObjid()
                    dirid = SpaceFragment.getcurrentdir()
                    userid = Someutil.getUserID()
                    id = System.currentTimeMillis().toString()

                }
                AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)

            }

            paths.toString().LogD(" paths : ")


        }
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
            I.toolbar_qiehuan -> {
                img_page_back.hide()
                toolbar_qiehuan.Show()

                if (main_bar.visibility != View.GONE) {
                    when (main_vp.currentItem) {
                        0 -> {
                            RxBus.get().post("changespace")

                        }

                        2 -> {

                            RxBus.get().post("collect_chose")

                        }


                    }
                }
                else{
                    RxBus.get().post("cancel")
                    toolbar_qiehuan.text = "切换"

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

            I.zhezhao -> {
                zhezhao.hide()
                RxBus.get().post("hidecardview")


            }
            I.img_page_back -> {



                RxBus.get().post("pback")

            }


        }

    }

}
