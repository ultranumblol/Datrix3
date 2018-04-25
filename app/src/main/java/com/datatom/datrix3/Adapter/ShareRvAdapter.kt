package com.datatom.datrix3.Adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Bean.ShareList
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.GildeLoader
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.helpers.D
import com.datatom.datrix3.helpers.RxBus
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by wgz 空间页面数据适配
 */
class ShareRvAdapter(context: Context) : RecyclerArrayAdapter<ShareList.Files>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return SpaceRvAdapterViewholder(parent)
    }


    private inner class SpaceRvAdapterViewholder(itemView: ViewGroup) : BaseViewHolder<ShareList.Files>(itemView, R.layout.item_sapcervadapter) {


        private var img: ImageView? = null

        private var filename: TextView? = null

        private var filecreatetime: TextView? = null


        private var checkboxIMG: ImageView? = null


        init {


            img = `$`(R.id.iv_thum)

            filename = `$`(R.id.tv_filename)

            filecreatetime = `$`(R.id.tv_date)

            checkboxIMG = `$`(R.id.checkbox_img)


        }

        override fun setData(data: ShareList.Files?) {


            // data!!.cayman_pretreat_mimetype.LogD(" type : ")
            var url = HttpUtil.BASEAPI_URL + "datrix3/viewer/read.php?type=thumb&fileid=" + data!!.fileid +
                    "&objectid=" + data.objid + "&createuid=" + data.createuid


            when (data!!.type) {
                "0" -> {
                    img!!.setImageResource(R.drawable.ic_file_wenjianjia)

                    // data.filename.LogD(" filename : ")

                    filecreatetime!!.text = data?.createtime
                }

                "1" -> {

                    filecreatetime!!.text = SizeUtils.getSize(data?.filesize) + "  " + data?.createtime

                    if (data!!.mimetype != null)
                        when (data!!.mimetype) {

                        //"png", "bmp", "gif", "jpeg",
                            "image" -> {
                                GildeLoader.loadNormal(img!!,url,D.ic_file_img)

                            }

                        //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                            "video"
                            -> {
                                GildeLoader.loadNormal(img!!,url,D.ic_file_video)

                            }

                        // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                            "audio"
                            -> {
                                GildeLoader.loadNormal(img!!,url,D.ic_file_mic)
                            }

                            "conf", "cpp", "htm", "html", "log", "sh", "txt", "xml", "pdf"
                            -> {
                                GildeLoader.loadNormal(img!!,url,D.ic_file_doc)

                            }
                            "zip", "7z", "rar", "tar" -> {
                                GildeLoader.loadNormal(img!!,url,D.ic_file_zip)

                            }
                            else -> {
                                GildeLoader.loadNormal(img!!,url,D.ic_file_doc)

                            }



                        }
                    else {
                        img!!.setImageResource(R.drawable.ic_file_doc)
                    }
                }

            }

            filename!!.text = data?.filename

        }
    }
}