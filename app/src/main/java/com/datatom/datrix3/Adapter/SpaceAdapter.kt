package com.datatom.datrix3.Adapter;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import android.util.SparseBooleanArray
import android.view.View
import android.widget.*

import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.Activities.ViewBigImageActivity
import com.datatom.datrix3.Activities.PlayVideoActivity
import com.datatom.datrix3.helpers.*


/**
 * Created by wgz
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


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return SpaceAdapterViewholder(parent)
    }


    private inner class SpaceAdapterViewholder(itemView: ViewGroup) : BaseViewHolder<PersonalFilelistData.result2>(itemView, R.layout.item_sapceadapter) {
        private var checkbox: CheckBox? = null

       // private var root: LinearLayout? = null

        private var img: ImageView? = null

        private var filename: TextView? = null

        private var filecreatetime: TextView? = null

        private var filesize: TextView? = null


        init {


            checkbox = `$`(R.id.item_collect_cb)

           // root = `$`(R.id.ll_root)
            // text = `$`<TextView>(R.id.item_text) ll_root

            img = `$`(R.id.iv_thum)

            filename = `$`(R.id.tv_filename)

            filecreatetime = `$`(R.id.tv_date)

            filesize = `$`(R.id.tv_filesize)

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




            filecreatetime!!.text = data?.createtime

            //data!!.cayman_pretreat_mimetype.LogD(" type : ")




            when (data!!.type) {
                "0" ->{
                    img!!.setImageResource(R.drawable.ic_file_wenjianjia)

                    filesize!!.hide()

                }


                "1" ->


                    when (data!!.cayman_pretreat_mimetype) {

                        //"png", "bmp", "gif", "jpeg",
                        "image" -> {
                            img!!.setImageResource(D.ic_file_img)


                            val imglist = arrayListOf<String>(data.fileid)

//                            root!!.setOnClickListener {
//                                data.toString().LogD("click : ")
//
//                                val bundle = Bundle()
//                                bundle.apply {
//
//                                    putInt("selet", 1)// 2 大图显示当前页数; 1,头像，不显示页数
//                                    putInt("code", 1)//第几张
//                                    putStringArrayList("imageuri", imglist)
//                                }
//
//
//                                context.startActivity(Intent(context, ViewBigImageActivity::class.java).putExtras(bundle).putExtra("file",data!!))
//                            }


                        }

                        //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                        "video"
                        -> {
                            img!!.setImageResource(D.ic_file_video)
//                            root!!.setOnClickListener {
//
//                                data.toString().LogD("click : ")
//
//                                context.startActivity(Intent(context, PlayVideoActivity::class.java)
//                                        .putExtra("file",data))
//
//                            }

                        }

                       // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                        "aideo"
                        -> {
                            img!!.setImageResource(D.ic_file_mic)
                        }

                        "conf", "cpp", "htm", "html", "log", "sh", "txt", "xml"
                        -> {
                            img!!.setImageResource(D.ic_file_doc)

                        }
                        "zip" -> {
                            img!!.setImageResource(D.ic_file_zip)

                        }


                    }


            }

            filename!!.text = data?.filename


            filesize!!.text = SizeUtils.getSize(data?.filesize)


//            if (filesize!!.text.toString().contains("0B")){
//                filesize!!.hide()
//            }




            checkbox!!.setOnCheckedChangeListener { _, isChecked ->
                // Log.d("wgz", "adapter : " + adapterPosition)
                if (isChecked) {
                    mCheckStates.put(adapterPosition, true)

                    RxBus.get().post("updatespacecheckbox")
                } else {
                    mCheckStates.delete(adapterPosition)
                    RxBus.get().post("updatespacecheckbox")
                }
            }

            checkbox!!.isChecked = mCheckStates.get(adapterPosition, false)


        }
    }
}
