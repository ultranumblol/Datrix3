package com.datatom.datrix3.Activities

import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Bean.ShareList
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.hide
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_preview_txtfile.*
import java.io.File


class PreviewTXTFileActivity : BaseActivity() {


    override fun ActivityLayout(): Int {

        return R.layout.activity_preview_txtfile
    }

    override fun initView() {



        loadData()
    }

    private fun loadData() {

        var data = intent.getSerializableExtra("file")

        when(data){
            is PersonalFilelistData.result2 ->{

                var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + data.fileid + "&iswindows=0&optuser=admin"

                setToolbartitle(data.filename)
                HttpUtil.instance.downLoadApi().downloadFileWithFixedUrl(url)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe({

                            var txtstr = Someutil.getTXTFileString(it.byteStream())
                            runOnUiThread {
                                pro_loading.hide()
                                tv_txtpreview.text =txtstr
                            }
                        }, {
                            it.toString().LogD("download error : ")
                        })
            }

            is TaskFile ->{
                setToolbartitle(data.filename)
                var txtstr = Someutil.getTXTFileString(File(data.filePath).inputStream())
                runOnUiThread {
                    pro_loading.hide()
                    tv_txtpreview.text =txtstr
                }


            }
            is ShareList.Files ->{
                var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + data.fileid + "&iswindows=0&optuser=admin"

                setToolbartitle(data.filename)
                HttpUtil.instance.downLoadApi().downloadFileWithFixedUrl(url)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe({

                            var txtstr = Someutil.getTXTFileString(it.byteStream())
                            runOnUiThread {
                                pro_loading.hide()
                                tv_txtpreview.text =txtstr
                            }
                        }, {
                            it.toString().LogD("download error : ")
                        })
            }

        }
    }
}
