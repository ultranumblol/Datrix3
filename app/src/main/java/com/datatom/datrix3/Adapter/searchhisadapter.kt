package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.datatom.datrix3.Bean.SearchHis
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import kotlinx.android.synthetic.main.item_searchhisadapter.*

/**
 * Created by wgz
 */
class searchhisadapter(context: Context) : RecyclerArrayAdapter<SearchHis>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return searchhisadapterViewholder(parent)
    }

    private inner class searchhisadapterViewholder(itemView: ViewGroup) : BaseViewHolder<SearchHis>(itemView, R.layout.item_searchhisadapter) {
        private var hisstr : TextView? = null
        private var hisid : TextView? = null

        init {
            hisstr = `$`(R.id.search_his_str)

            hisid = `$`(R.id.search_his_id)
        }

        override fun setData(data: SearchHis?) {

            hisstr!!.text = data!!.searchstr

            hisid!!.text = data!!.id.toString()
        }

    }
}
