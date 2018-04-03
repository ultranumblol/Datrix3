package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import kotlinx.android.synthetic.main.item_openshareadapter.*

/**
 * Created by wgz
 */
class openshareadapter(context: Context) : RecyclerArrayAdapter<Any>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return openshareadapterViewholder(parent)
    }

    private inner class openshareadapterViewholder(itemView: ViewGroup) : BaseViewHolder<Any>(itemView, R.layout.item_openshareadapter) {
        private var img_download : ImageView? =null
        private var img_delete : ImageView? =null
        private var img_code : ImageView? =null

        init {

            img_download = `$`<ImageView>(R.id.img_share_download)
            img_delete = `$`<ImageView>(R.id.img_share_delete)
            img_code = `$`<ImageView>(R.id.img_share_code)

            //img = `$`<ImageView>(R.id.item_img)

            // text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: Any?) {


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
