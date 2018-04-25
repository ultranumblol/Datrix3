package com.datatom.datrix3.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import collections.forEach
import com.datatom.datrix3.Adapter.sharedfilesadapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.ShareList
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Service.TaskService
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.*
import com.jude.easyrecyclerview.EasyRecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_share_dfiles.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


class SharedFilesActivity : BaseActivity() {

    private var rvadapter: sharedfilesadapter? = null

    private var files: ShareList.Source? = null


    override fun initView() {


        files = intent.getSerializableExtra("files") as ShareList.Source?


        setToolbartitle("文件分享")
        rvadapter = sharedfilesadapter(this)

        rv_sharedfiles.apply {
            setLayoutManager(LinearLayoutManager(this@SharedFilesActivity))
            adapter = rvadapter

        }
        rvadapter!!.apply {
            addAll(files!!.files)
            setOnItemClickListener {
                if (rvadapter!!.allData[it].mimetype != null)
                    when (rvadapter!!.allData[it].mimetype) {
                        "image" -> {

                            val imglist = arrayListOf<String>(rvadapter!!.allData[it].fileid)

                            val bundle = Bundle()
                            bundle.apply {

                                putInt("selet", 1)// 2 大图显示当前页数; 1,头像，不显示页数
                                putInt("code", 1)//第几张
                                putStringArrayList("imageuri", imglist)
                            }
                            context!!.startActivity(Intent(context, ViewBigImageActivity::class.java).putExtras(bundle).putExtra("file", rvadapter!!.allData[it]!!))
                        }

                    //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                        "video"
                        -> {
                            // rvadapter!!.allData[it].toString().LogD("click : ")
                            context!!.startActivity(Intent(context, PlayVideoActivity::class.java)
                                    .putExtra("file", rvadapter!!.allData[it]))
                        }

                    // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                        "audio"
                        -> {
                        }

                        "conf", "cpp", "htm", "html", "log", "sh", "xml"
                        -> {

                        }
                        "txt" -> {
                            context!!.startActivity(Intent(context, PreviewTXTFileActivity::class.java)
                                    .putExtra("file", rvadapter!!.allData[it]))
                        }
                        "zip" -> {

                        }
                        "pdf" -> {

                            context!!.startActivity(Intent(context, PDFViewerActivity::class.java)
                                    .putExtra("file", rvadapter!!.allData[it]))

                        }
                        "doc" -> {
//                            context!!.startActivity(Intent(context, OfficeFileShowActivity::class.java)
//                                    .putExtra("file", rvadapter!!.allData[it]))
                        }
                        else -> {

                        }
                    }
            }


        }

        rl_code.setOnClickListener {
            var qrimage = View.inflate(this, R.layout.dialog_qr_image, null)
            var image = qrimage.find<ImageView>(I.dialog_image)
            image.createQRImage(files!!.shareurl)

            AlertDialog.Builder(this).apply {
                setView(qrimage)
                show()
            }

        }

        //下载
        rl_download.setOnClickListener {

            rvadapter!!.getcheckboxArrary().forEach { i, _ ->

                var taskfile = TaskFile()
                taskfile!!.apply {

                    filename = rvadapter!!.allData[i].filename
                    fileid = rvadapter!!.allData[i].fileid
                    mCompeleteSize = 0L
                    offset = 0
                    if (rvadapter!!.allData[i].mimetype != null)
                        mimetype = rvadapter!!.allData[i].mimetype
                    exe = rvadapter!!.allData[i].ext
                    filetype = TaskService.DOWNLOAD
                    filesubtype = TaskService.NORMALDOWNLOAD
                    taskstate = TaskService.NEWFILE
                    total = rvadapter!!.allData[i].filesize.toLong()
                    userid = Someutil.getUserID()
                    id = System.currentTimeMillis().toString()

                }
                AppDatabase.getInstance(app.mapp).TaskFileDao().insert(taskfile)

            }

            //todo 添加网络类型判断
//                context.let {
//                    AlertDialog.Builder(it!!)
//                            .run {
//                                setMessage("你即将使用手机流量传输文件，继续传输将产生流量费用")
//                                setNegativeButton("仅WI-FI传输") { _, _ ->
//
//
//                                    //
//
//                                }
//                                setPositiveButton("手机流量传输") { _, _ -> }
//                                show()
//                            }.apply {
//                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(C.colorPrimary))
//                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(C.colorPrimary))
//
//                    }
//
//                }
            rvadapter!!.setCheckBoxNoneSelect()
            RxBus.get().post("updatesharedfilecheckbox")
            this!!.toast("开始后台下载")

        }

        RxBus.get().toFlowable(String::class.java).subscribe {

            when(it){
                "updatesharedfilecheckbox" ->{
                    if (rvadapter!!.getcheckboxArrary().size() > 0) {
                        bottom_bar!!.Show()
                        rvadapter!!.notifyDataSetChanged()


                    } else {
                       // RxBus.get().post("showmainbar")

                        bottom_bar!!.hide()
                        rvadapter!!.notifyDataSetChanged()
                    }

                }
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.shared_file_allselect, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_allselect) {

            rvadapter!!.apply {
                if (rvadapter!!.getcheckboxArrary().size() > 0 && rvadapter!!.getcheckboxArrary().size() == rvadapter!!.count) {
                    setCheckBoxNoneSelect()
                    notifyDataSetChanged()
                    RxBus.get().post("updatesharedfilecheckbox")
                } else {
                    setCheckBoxAllSelect()
                    notifyDataSetChanged()
                    RxBus.get().post("updatesharedfilecheckbox")
                }
            }


        }

        return super.onOptionsItemSelected(item)
    }


    override fun ActivityLayout(): Int {

        return R.layout.activity_share_dfiles
    }
}
