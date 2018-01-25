package com.datatom.datrix3.Adapter;

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.CheckBox
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import android.util.SparseBooleanArray
import android.widget.CompoundButton
import com.datatom.datrix3.helpers.RxBus


/**
 * Created by wgz
 */
class SpaceAdapter(context: Context) : RecyclerArrayAdapter<Any>(context) {

    var mCheckStates = SparseBooleanArray()


    fun getcheckboxArrary(): SparseBooleanArray {

        return this.mCheckStates

    }


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return SpaceAdapterViewholder(parent)
    }


    private inner class SpaceAdapterViewholder(itemView: ViewGroup) : BaseViewHolder<Any>(itemView, R.layout.item_sapceadapter) {
        private var checkbox: CheckBox? = null


        init {


            checkbox = `$`<CheckBox>(R.id.item_collect_cb)

            // text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: Any?) {


            checkbox!!.setOnCheckedChangeListener { _, isChecked ->
                Log.d("wgz", "adapter : " + adapterPosition)
                if (isChecked) {
                    mCheckStates.put(adapterPosition, true)

                    RxBus.get().post("updatecheckbox")
                } else {
                    mCheckStates.delete(adapterPosition)
                    RxBus.get().post("updatecheckbox")
                }
            }

            checkbox!!.isChecked = mCheckStates.get(adapterPosition, false)


        }
    }
}
