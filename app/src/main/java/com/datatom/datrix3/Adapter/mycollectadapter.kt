package com.datatom.datrix3.Adapter;

import android.app.AlertDialog
import android.content.Context
import android.util.SparseBooleanArray
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.datatom.datrix3.Bean.CollectFiles
import com.datatom.datrix3.Bean.ListIndex
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.GildeLoader
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.D
import com.datatom.datrix3.helpers.RxBus
import org.jetbrains.anko.toast


/**
 * Created by wgz
 */
class mycollectadapter(context: Context) : RecyclerArrayAdapter<CollectFiles.result.hits.hits2>(context) {
    var mCheckStates = SparseBooleanArray()

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return mycollectadapterViewholder(parent)
    }

    private inner class mycollectadapterViewholder(itemView: ViewGroup) : BaseViewHolder<CollectFiles.result.hits.hits2>(itemView, R.layout.item_mycollectadapter2) {

        private var root: LinearLayout? = null

        private var img: ImageView? = null

        private var filename: TextView? = null

        private var filecreatetime: TextView? = null


        private var checkboxIMG: ImageView? = null


        init {
            root = `$`(R.id.ll_item)
            img = `$`(R.id.iv_thum)

            filename = `$`(R.id.tv_filename)

            filecreatetime = `$`(R.id.tv_date)

            checkboxIMG = `$`(R.id.checkbox_img)

        }

        override fun setData(data: CollectFiles.result.hits.hits2?) {

            var url = HttpUtil.BASEAPI_URL + "datrix3/viewer/read.php?type=thumb&fileid=" + data!!._source.fileid +
                    "&objectid=" + data._source.objid + "&createuid=" + data._source.createuid



            when (data!!._source.type) {
                "0" -> {
                    img!!.setImageResource(R.drawable.ic_file_wenjianjia)

                    // data.filename.LogD(" filename : ")

                    filecreatetime!!.text = data?._source.createtime
                }

                "1" -> {

                    filecreatetime!!.text = data?._source.createtime
                    if (data!!._source.cayman_pretreat_mimetype != null)
                        when (data!!._source.cayman_pretreat_mimetype) {

                        //"png", "bmp", "gif", "jpeg",
                            "image" -> {
                                GildeLoader.loadNormal(img!!, url, D.ic_file_img)

                            }

                        //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                            "video"
                            -> {
                                GildeLoader.loadNormal(img!!, url, D.ic_file_video)

                            }

                        // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                            "audio"
                            -> {
                                GildeLoader.loadNormal(img!!, url, D.ic_file_mic)
                            }

                            "conf", "cpp", "htm", "html", "log", "sh", "txt", "xml", "pdf"
                            -> {
                                GildeLoader.loadNormal(img!!, url, D.ic_file_doc)

                            }
                            "zip", "7z", "rar", "tar" -> {
                                GildeLoader.loadNormal(img!!, url, D.ic_file_zip)

                            }
                            else -> {
                                GildeLoader.loadNormal(img!!, url, D.ic_file_doc)

                            }

                        }
                    else {
                        img!!.setImageResource(R.drawable.ic_file_doc)
                    }
                }

            }

            filename!!.text = data?._source.filename



            checkboxIMG!!.apply {

                setImageResource(R.drawable.ic_star_blue_grey_500_24dp)


                setOnClickListener {
                    AlertDialog.Builder(context)
                            .setTitle("是否取消此文件的收藏？")
                            .setPositiveButton("确定",{_,_ ->
                                HttpUtil.instance.apiService().collect_removeby(Someutil.getToken(), data._source.fileid, Someutil.getUserID())
                                        .compose(RxSchedulers.compose())
                                        .subscribe({
                                            if (it.contains("200")){


                                                RxBus.get().post(ListIndex(adapterPosition,"collection"))
                                                context.toast("已取消收藏")
                                            }
                                            else{
                                                context.toast("服务器错误！")
                                            }
                                        }, {
                                            context.toast("网络错误！")
                                        })

                            })
                            .setNegativeButton("取消",{_,_ ->})
                            .show()


                }

            }
        }
    }
}
