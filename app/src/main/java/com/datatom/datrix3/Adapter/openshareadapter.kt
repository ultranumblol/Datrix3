package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.Bean.ShareList
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.helpers.I
import kotlinx.android.synthetic.main.item_openshareadapter.*

/**
 * Created by wgz
 */
class openshareadapter(context: Context) : RecyclerArrayAdapter<ShareList.Hits>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return openshareadapterViewholder(parent)
    }

    private inner class openshareadapterViewholder(itemView: ViewGroup) : BaseViewHolder<ShareList.Hits>(itemView, R.layout.item_openshareadapter) {
        private var img_download : ImageView? =null
        private var img_delete : ImageView? =null
        private var img_code : ImageView? =null

        private var whosharefile : TextView? = null
        private var sharetime : TextView?  =null

        init {

            img_download = `$`(R.id.img_share_download)
            img_delete = `$`(R.id.img_share_delete)
            img_code = `$`(R.id.img_share_code)

            whosharefile = `$`(I.tv_who_share_file)
            sharetime = `$`(I.tv_sharetime)


            //img = `$`<ImageView>(R.id.item_img)

            // text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: ShareList.Hits?) {


            sharetime!!.text =data!!._source.ctime
            whosharefile!!.text = data._source.shareuid +"分享了一个文件"

            img_download!!.apply {
               setOnClickListener {  }

            }
            img_delete!!.apply {
                setOnClickListener {  }

            }
            img_code!!.apply {
                setOnClickListener {  }

            }




        }

    }
}
