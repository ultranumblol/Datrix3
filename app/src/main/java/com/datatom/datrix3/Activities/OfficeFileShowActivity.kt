package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.*
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.IntentUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus
import com.datatom.datrix3.helpers.hide
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_office_file_show.*
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.concurrent.TimeUnit

import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.text.TextUtils
import android.widget.RelativeLayout
import com.tencent.smtt.sdk.TbsReaderView
import java.io.File


/**
 * office文件下载预览
 */
class OfficeFileShowActivity : BaseActivity() ,TbsReaderView.ReaderCallback{
    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

    }

    private var mTbsReaderView: TbsReaderView? = null
    private lateinit var database: AppDatabase

    override fun ActivityLayout(): Int {

        return R.layout.activity_office_file_show
    }

    override fun initView() {
        mTbsReaderView = TbsReaderView(this, this)
        var data = intent.getSerializableExtra("file")
        database = AppDatabase.getInstance(this)
        rl_file.addView(mTbsReaderView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

        when(data){
            is PersonalFilelistData.result2 ->{
                file_downloadProgress.progress = 0
                setToolbartitle(data.filename)
                //data.size.toInt().toString().LogD(" size :: ")
                DownfileAndOpen(data, OfficeFile(data.fileid, data.filename, 0, data.size.toInt(), data.fileid))

                val updatesub = Observable.interval(1, TimeUnit.SECONDS).compose(RxSchedulers.compose<Long>())
                        .subscribe {

                            file_downloadProgress.progress = database.OfficefileDao().queryofficeifle(data.fileid).progress

                            if (database.OfficefileDao().queryofficeifle(data.fileid).progress == 100) {

                            }
//
                        }

                addsub(updatesub)
            }

            is TaskFile ->{
                file_downloadProgress.visibility = View.INVISIBLE
                openfile(data)

            }

            is ShareList.Files ->{


            }
            is SearchResultData.res ->{

            }
        }





    }

    private fun openFile(path: String) :Boolean {
        val tempPath = File("${Environment.getExternalStorageDirectory()}/officetemp")
        if (!tempPath.exists())
            tempPath.mkdirs()
        //通过bundle把文件传给x5,打开的事情交由x5处理
        val bundle = Bundle()
        //传递文件路径
        bundle.putString("filePath", File(path).toString())
        //加载插件保存的路径
        bundle.putString("tempPath",  "${Environment.getExternalStorageDirectory()}/officetemp")
        if (this.mTbsReaderView == null)
            this.mTbsReaderView = TbsReaderView(this, this)
        //加载文件前的初始化工作,加载支持不同格式的插件
        val b = mTbsReaderView!!.preOpen(getFileType(File(path).toString()), false)
        if (b) {
            mTbsReaderView!!.openFile(bundle)
        }

        return b


    }



    private fun getFileType(path: String): String {
        var str = ""

        if (TextUtils.isEmpty(path)) {
            return str
        }
        val i = path.lastIndexOf('.')
        if (i <= -1) {
            return str
        }
        str = path.substring(i + 1)
        return str
    }


    private fun openfile(data: TaskFile) {

        try {
            when (data.exe) {

                "docx" -> {
                    this!!.startActivity(IntentUtil.getWordFileIntent(data.filePath))

                }
                "doc" -> {
                    this!!.startActivity(IntentUtil.getWordFileIntent(data.filePath))

                }
                "xls" -> {
                    this!!.startActivity(IntentUtil.getExcelFileIntent(data.filePath))
                }
                "xlsx" -> {
                    this!!.startActivity(IntentUtil.getExcelFileIntent(data.filePath))
                }
                "ppt" -> {
                    this!!.startActivity(IntentUtil.textopen(data.filePath, "ppt"))
                }
                "pptx" -> {
                    this!!.startActivity(IntentUtil.getPptFileIntent(data.filePath))
                }
                "txt" -> {
                    this!!.startActivity(IntentUtil.getTextFileIntent(data.filePath, false))
                }
                else -> "未找到类型"

            }
        } catch (e :Exception){
            this.toast("未找到文件！")

        }


    }


    private fun DownfileAndOpen(data: PersonalFilelistData.result2, officeFile: OfficeFile) {

        database.OfficefileDao().insert(OfficeFile(data.fileid, data.filename, 0, data.size.toInt(), data.fileid))

        var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + data.fileid + "&iswindows=0&optuser=admin"

        url.LogD(" 下载URL ： ")
        val futureStudioIconFile = File(Environment.getExternalStorageDirectory().toString() + File.separator
                + "datrixdownload")
        if (!futureStudioIconFile.exists())
            futureStudioIconFile.mkdirs()
        val file = File(futureStudioIconFile,  data.filename)
        if (file.exists()){
            when (data.ext) {

                "docx" -> {
                    //
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                        runOnUiThread {
                                downfile_name.text = "没有可打开的应用！"
                                file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                "doc" -> {
                    //
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                        runOnUiThread {
                            downfile_name.text = "没有可打开的应用！"
                            file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                "xls" -> {
                    // this!!.startActivity(IntentUtil.getExcelFileIntent(file.absolutePath))
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                        runOnUiThread {
                            downfile_name.text = "没有可打开的应用！"
                            file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                "xlsx" -> {
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.getExcelFileIntent(file.absolutePath))
                        runOnUiThread {
                            downfile_name.text = "没有可打开的应用！"
                            file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                "ppt" -> {
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.textopen(file.absolutePath, "ppt"))
                        runOnUiThread {
                            downfile_name.text = "没有可打开的应用！"
                            file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                "pptx" -> {
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.getPptFileIntent(file.absolutePath))
                        runOnUiThread {
                            downfile_name.text = "没有可打开的应用！"
                            file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                "txt" -> {
                    if (!openFile(file.absolutePath)){
                        this!!.startActivity(IntentUtil.getTextFileIntent(file.absolutePath, false))
                        runOnUiThread {
                            downfile_name.text = "没有可打开的应用！"
                            file_downloadProgress.visibility = View.INVISIBLE
                        }
                    }
                }
                else -> "未找到类型"

            }
        }else{
            val download  = HttpUtil.instance.downLoadApi(officeFile).downloadFileWithFixedUrl(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .subscribe({

                        var result = Someutil.writeResponseBodyToDisk2(it, data.filename)
                        var file = result.second

                        if (result.first) {
                            "下载成功".LogD()
                            when (data.ext) {

                                "docx" -> {
                                    //
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                "doc" -> {
                                    //
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                "xls" -> {
                                    // this!!.startActivity(IntentUtil.getExcelFileIntent(file.absolutePath))
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                "xlsx" -> {
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.getExcelFileIntent(file.absolutePath))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                "ppt" -> {
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.textopen(file.absolutePath, "ppt"))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                "pptx" -> {
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.getPptFileIntent(file.absolutePath))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                "txt" -> {
                                    if (!openFile(file.absolutePath)){
                                        this!!.startActivity(IntentUtil.getTextFileIntent(file.absolutePath, false))
                                        runOnUiThread {
                                            downfile_name.text = "没有可打开的应用！"
                                            file_downloadProgress.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                                else -> "未找到类型"

                            }

                        } else {
                            "下载失败".LogD()

                        }
                    }, {
                        it.toString().LogD("download error : ")
                        runOnUiThread {
                            if (it.toString().contains("No Activity found")) {
                                downfile_name.text = "没有找到打开此文件的应用！"
                                file_downloadProgress.visibility = View.INVISIBLE

                            }

                        }

                    })

            addsub(download)
        }



    }
}
