package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.Bean.MoreItems
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

import com.datatom.datrix3.R



/**
 * Created by wgz
 */
class MoreFragmentadapter(context: Context) : RecyclerArrayAdapter<MoreItems>(context) {



    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MoreFragmentadapterViewholder(parent)
    }

    private inner class MoreFragmentadapterViewholder(itemView: ViewGroup) : BaseViewHolder<MoreItems>(itemView, R.layout.item_morefragmentadapter) {

        private val img : ImageView

        private val text : TextView

        init {
            img = `$`<ImageView>(R.id.item_img)

            text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: MoreItems?) {
            img.setImageResource(data!!.icon)

            text.text = data.name


        }

    }
}
