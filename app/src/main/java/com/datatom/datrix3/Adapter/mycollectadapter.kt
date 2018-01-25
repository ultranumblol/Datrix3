package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R



/**
 * Created by wgz
 */
class mycollectadapter(context: Context) : RecyclerArrayAdapter<Any>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return mycollectadapterViewholder(parent)
    }

    private inner class mycollectadapterViewholder(itemView: ViewGroup) : BaseViewHolder<Any>(itemView, R.layout.item_mycollectadapter2) {

        private  var root : LinearLayout? = null

        init {
            root = `$`<LinearLayout>(R.id.ll_item)


        }

        override fun setData(data: Any?) {

            root!!.setOnClickListener {
                Toast.makeText(context,"123",Toast.LENGTH_LONG).show()

            }

        }

    }
}
