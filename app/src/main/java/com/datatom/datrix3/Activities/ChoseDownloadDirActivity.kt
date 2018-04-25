package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.datatom.datrix3.Adapter.dirsadapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.FileDir
import com.datatom.datrix3.Util.SPBuild
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.base.AppConstant
import com.datatom.datrix3.helpers.I
import kotlinx.android.synthetic.main.activity_chose_download_dir.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.io.File


class ChoseDownloadDirActivity : BaseActivity() {

    private var listFileName: ArrayList<FileDir>? = null
    private var rvadapter: dirsadapter? = null
    private var dirslist: ArrayList<String>? = null

    override fun ActivityLayout(): Int {

        return R.layout.activity_chose_download_dir
    }

    override fun initView() {
        setToolbartitle("设置下载目录")
        rv_dirs.setLayoutManager(LinearLayoutManager(this))
        rvadapter = dirsadapter(this)
        rv_dirs.adapter = rvadapter

        current_dirs.text = "当前下载目录:${ Someutil.getDownloaddirs()}"
        listFileName = arrayListOf()
        dirslist = arrayListOf()
        dirslist!!.add(Environment.getExternalStorageDirectory().toString())

        scan(File(dirslist!![0]))
        rvadapter!!.apply {
            setOnItemClickListener {
                dirslist!!.add(allData[it].path)
                clear()
                scan(File(dirslist!![dirslist!!.size - 1]))

            }
        }
        go_parentdir.setOnClickListener {

            if (dirslist!!.size > 1){
                rvadapter!!.clear()
                dirslist!!.removeAt(dirslist!!.size - 1)
                scan(File(dirslist!!.get(dirslist!!.size - 1)))

            }

        }

    }


    fun scan(file: File) {
        if (file.isDirectory)// 判断下是否为文件夹
        {
            listFileName!!.clear()
            var files = file.listFiles()

            if (files != null) {
                files.forEach {

                    if (it.isDirectory) {
                        listFileName!!.add(FileDir(it.name, it.absolutePath, it.parentFile))
                    }
                }
            }
            rvadapter!!.addAll(listFileName)
            // adapter.setData(listFileName)；//获取完该文件的所有文件夹之后刷新一下listview的显示数据
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.new_dir, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.new_dirs -> {
                var editviewlayout = View.inflate(this, R.layout.dialog_edittext_dabaoxiazai, null)
                var editview = editviewlayout.find<EditText>(I.dialog_edittext)
                AlertDialog.Builder(this)
                        .apply {
                            setTitle("请输入文件夹名称：")
                            setView(editviewlayout)
                            setPositiveButton("确定", { _, _ ->
                                File(dirslist!!.get(dirslist!!.size - 1)+ "/${editview.text}").mkdirs()
                                rvadapter!!.clear()
                                scan(File(dirslist!![dirslist!!.size - 1]))
                            })
                            show()
                        }
            }
            R.id.new_dirs_save -> {

                AlertDialog.Builder(this)
                        .apply{
                            setTitle("请确认")
                            setMessage("是否将${dirslist!![dirslist!!.size - 1]} 设置为下载目录？")
                            setPositiveButton("确定", {_,_ ->
                                SPBuild(applicationContext)
                                        .addData(AppConstant.DOWNLOADDIRS, dirslist!![dirslist!!.size - 1])
                                        .build()
                                current_dirs.text = "当前下载目录:${ Someutil.getDownloaddirs()}"
                                this@ChoseDownloadDirActivity.toast("设置成功")
                                this@ChoseDownloadDirActivity.finish()

                            })
                            setNegativeButton("取消",{_,_ ->})
                            show()

                        }

            }


//            val futureStudioIconFile = File(Environment.getExternalStorageDirectory().toString() + File.separator
//                    + "datrixdownload")
//            if (!futureStudioIconFile.exists())
//                futureStudioIconFile.mkdirs()
        }


        return super.onOptionsItemSelected(item)
    }
}
