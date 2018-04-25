package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.datatom.datrix3.Bean.FileDir
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import kotlinx.android.synthetic.main.item_dirsadapter.*

/**
 * Created by wgz
 */
class dirsadapter(context: Context) : RecyclerArrayAdapter<FileDir>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return dirsadapterViewholder(parent)
    }

    private inner class dirsadapterViewholder(itemView: ViewGroup) : BaseViewHolder<FileDir>(itemView, R.layout.item_dirsadapter) {
        private var filename : TextView? = null
        init {
            filename = `$`<TextView>(R.id.tv_filename)

            // text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: FileDir?) {

            filename!!.text = data!!.name
        }

    }
}
