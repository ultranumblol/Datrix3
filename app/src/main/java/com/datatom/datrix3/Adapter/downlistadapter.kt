package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.Bean.TaskFile
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.view.MyProgressBar
import kotlinx.android.synthetic.main.item_downlistadapter.*

/**
 * Created by wgz
 */
class downlistadapter(context: Context) : RecyclerArrayAdapter<TaskFile>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return downlistadapterViewholder(parent)
    }

    private inner class downlistadapterViewholder(itemView: ViewGroup) : BaseViewHolder<TaskFile>(itemView, R.layout.item_downlistadapter) {

        private var name : TextView? = null

        private var loadsize : TextView? = null

        private var pro : ImageView? = null
        init {

            name = `$`(I.nameTextView)

            loadsize = `$`(I.descTextView)

            pro = `$`(I.id_myprobar)

        }

        override fun setData(data: TaskFile?) {

            name!!.text = data?.filename

            loadsize!!.text = SizeUtils.getSize(data?.mCompeleteSize!!) + "/" + SizeUtils.getSize(data?.total)

            //pro!!.setProgress(data.filepersent.toFloat())
        }

    }
}
