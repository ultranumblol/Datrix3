package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.OfficeFile
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Bean.TaskFile
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

/**
 * office文件下载预览
 */
class OfficeFileShowActivity : BaseActivity() {




    private lateinit var database: AppDatabase

    override fun ActivityLayout(): Int {

        return R.layout.activity_office_file_show
    }

    override fun initView() {

        var data = intent.getSerializableExtra("file")
        database = AppDatabase.getInstance(this)

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
        }





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
                                this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                            }
                            "doc" -> {
                                this!!.startActivity(IntentUtil.getWordFileIntent(file.absolutePath))
                            }
                            "xls" -> {
                                this!!.startActivity(IntentUtil.getExcelFileIntent(file.absolutePath))
                            }
                            "xlsx" -> {
                                this!!.startActivity(IntentUtil.getExcelFileIntent(file.absolutePath))
                            }
                            "ppt" -> {
                                this!!.startActivity(IntentUtil.textopen(file.absolutePath, "ppt"))
                            }
                            "pptx" -> {
                                this!!.startActivity(IntentUtil.getPptFileIntent(file.absolutePath))
                            }
                            "txt" -> {
                                this!!.startActivity(IntentUtil.getTextFileIntent(file.absolutePath, false))
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
