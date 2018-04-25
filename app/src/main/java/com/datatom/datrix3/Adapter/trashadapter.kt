package com.datatom.datrix3.Adapter;

import android.content.Context
import android.util.SparseBooleanArray
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.Bean.TrashBean
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.helpers.D
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.RxBus
import kotlinx.android.synthetic.main.item_trashadapter.*

/**
 * Created by wgz
 */
class trashadapter(context: Context) : RecyclerArrayAdapter<TrashBean.result2>(context) {
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
        return trashadapterViewholder(parent)
    }

    private inner class trashadapterViewholder(itemView: ViewGroup) : BaseViewHolder<TrashBean.result2>(itemView, R.layout.item_trashadapter) {
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

        override fun setData(data: TrashBean.result2?) {
            when (data!!.type) {
                "0" -> {
                    img!!.setImageResource(R.drawable.ic_file_wenjianjia)

                    // data.filename.LogD(" filename : ")

                    filecreatetime!!.text = data?.trashdate
                }

                "1" -> {

                    filecreatetime!!.text = SizeUtils.getSize(data?.filesize) + "  " + data?.trashdate

                    if (data!!.cayman_pretreat_mimetype != null)
                        when (data!!.cayman_pretreat_mimetype) {

                        //"png", "bmp", "gif", "jpeg",
                            "image" -> {
                                img!!.setImageResource(D.ic_file_img)

                            }

                        //"3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4",
                            "video"
                            -> {
                                img!!.setImageResource(D.ic_file_video)

                            }

                        // "m3u", "m4a", "m4b", "m4p", "mp2", "mp3", "mpga", "ogg", "rmvb", "wav", "wmv",
                            "audio"
                            -> {
                                img!!.setImageResource(D.ic_file_mic)
                            }

                            "conf", "cpp", "htm", "html", "log", "sh", "txt", "xml", "pdf"
                            -> {
                                img!!.setImageResource(D.ic_file_doc)

                            }
                            "zip", "7z", "rar", "tar" -> {
                                img!!.setImageResource(D.ic_file_zip)

                            }
                            else -> {

                                img!!.setImageResource(D.ic_file_doc)
                            }


                        }
                    else {
                        img!!.setImageResource(D.ic_file_doc)
                    }
                }

            }

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
                            RxBus.get().post("updatetrashcheckbox")
                        }
                        true -> {
                            setImageResource(D.ic_remove_circle_outline_black_18dp)
                            mCheckStates.delete(adapterPosition)
                            RxBus.get().post("updatetrashcheckbox")
                        }

                    }

                }

            }

        }

    }
}
