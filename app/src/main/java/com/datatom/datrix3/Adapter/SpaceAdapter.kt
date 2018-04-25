package com.datatom.datrix3.Adapter;

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import android.util.SparseBooleanArray
import android.view.View
import android.widget.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.datatom.datrix3.Base.GlideApp
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Util.GildeLoader
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.*


/**
 * Created by wgz 空间页面数据适配
 */
class SpaceAdapter(context: Context) : RecyclerArrayAdapter<PersonalFilelistData.result2>(context) {

    var mCheckStates = SparseBooleanArray()


    fun getcheckboxArrary(): SparseBooleanArray {

        return this.mCheckStates

    }

    fun setCheckBoxAllSelect() {

        for (i in 0 until count) {
            mCheckStates.put(i, true)

        }


    }

    fun setCheckBoxNoneSelect() {

        for (i in 0 until count) {
            mCheckStates.delete(i)

        }

    }

    fun setItemChecked(position: Int) {

        when (mCheckStates.get(position, false)) {
            false -> {

                mCheckStates.put(position, true)
                RxBus.get().post("updatespacecheckbox")
            }
            true -> {

                mCheckStates.delete(position)
                RxBus.get().post("updatespacecheckbox")
            }

        }


    }


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return SpaceAdapterViewholder(parent)
    }


    private inner class SpaceAdapterViewholder(itemView: ViewGroup) : BaseViewHolder<PersonalFilelistData.result2>(itemView, R.layout.item_sapceadapter) {


        private var img: ImageView? = null

        private var filename: TextView? = null

        private var filecreatetime: TextView? = null


        private var checkboxIMG: ImageView? = null


        init {


            img = `$`(R.id.iv_thum)

            filename = `$`(R.id.tv_filename)

            filecreatetime = `$`(R.id.tv_date)

            checkboxIMG = `$`(I.checkbox_img)


        }

        /**
         * 文档1
        视频	2
        图片	3
        二进制数据	4
        音频	5
        文本	6
        资料集	1000
         */
        override fun setData(data: PersonalFilelistData.result2?) {


            // data!!.cayman_pretreat_mimetype.LogD(" type : ")

            var url = HttpUtil.BASEAPI_URL + "datrix3/viewer/read.php?type=thumb&fileid=" + data!!.fileid +
                    "&objectid=" + data.objid + "&createuid=" + data.createuid

            when (data!!.type) {
                "0" -> {
                    img!!.setImageResource(R.drawable.ic_file_wenjianjia)

//                    GlideApp.with(context).load(url)
//                            .transition(DrawableTransitionOptions().crossFade(200))
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .apply({ placeholder(D.ic_file_wenjianjia) })
//                            .listener(object : RequestListener<Drawable> {
//                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                                    return false
//                                }
//                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                                    e.toString().LogD(" loadfailed  error : ")
//                                    //  img!!.setImageResource(D.ic_file_img)
//                                    return false
//
//                                }
//                            }
//                            )
//                            .into(img!!)

                    filecreatetime!!.text = data?.createtime
                }

                "1" -> {

                    filecreatetime!!.text = SizeUtils.getSize(data?.filesize) + "  " + data?.createtime


                    if (data!!.cayman_pretreat_mimetype != null)
                        when (data!!.cayman_pretreat_mimetype) {

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
                        GildeLoader.loadNormal(img!!,url,D.ic_file_doc)
                    }
                }

            }

           // notifyDataSetChanged()

            filename!!.text = data?.filename

            checkboxIMG!!.apply {

                when (mCheckStates.get(adapterPosition, false)) {

                    true -> {
                        setImageResource(D.ic_filechecked)

                    }
                    false -> {
                        setImageResource(D.ic_remove_circle_outline_black_18dp)

                    }

                }

                setOnClickListener {

                    when (mCheckStates.get(adapterPosition, false)) {
                        false -> {
                            setImageResource(D.ic_filechecked)
                            mCheckStates.put(adapterPosition, true)
                            RxBus.get().post("updatespacecheckbox")
                        }
                        true -> {
                            setImageResource(D.ic_remove_circle_outline_black_18dp)
                            mCheckStates.delete(adapterPosition)
                            RxBus.get().post("updatespacecheckbox")
                        }

                    }

                }

            }

        }
    }
}
