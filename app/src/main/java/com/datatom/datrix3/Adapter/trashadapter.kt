package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import kotlinx.android.synthetic.main.item_trashadapter.*

/**
 * Created by wgz
 */
class trashadapter(context: Context) : RecyclerArrayAdapter<Any>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return trashadapterViewholder(parent)
    }

    private inner class trashadapterViewholder(itemView: ViewGroup) : BaseViewHolder<Any>(itemView, R.layout.item_trashadapter) {
        init {
            //img = `$`<ImageView>(R.id.item_img)

            // text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: Any?) {


        }

    }
}
