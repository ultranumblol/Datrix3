package com.datatom.datrix3.Adapter;

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.datatom.datrix3.Bean.CancelTask
import com.datatom.datrix3.Bean.TaskFile
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Service.TaskService.Companion.PAUSE
import com.datatom.datrix3.Service.TaskService.Companion.WRITING

import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.Util.UploadFileUtil2.Companion.DONE
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase


import com.datatom.datrix3.helpers.*


/**
 * Created by wgz
 */
class uploadadapter(context: Context) : RecyclerArrayAdapter<TaskFile>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return uploadadapterViewholder(parent)
    }

    private inner class uploadadapterViewholder(itemView: ViewGroup) : BaseViewHolder<TaskFile>(itemView, R.layout.item_downlistadapter) {
        private var name: TextView? = null

        private var loadsize: TextView? = null

        private var img: ImageView? = null

        private var root: RelativeLayout? = null


        init {

            name = `$`(I.nameTextView)

            loadsize = `$`(I.descTextView)

            img = `$`(I.id_myprobar)

            root = `$`(I.rootlayout)

        }

        override fun setData(data: TaskFile?) {

            name!!.text = data!!.filename


            loadsize!!.text = SizeUtils.getSize(data?.total * data.filepersent / 100) + "/" + SizeUtils.getSize(data?.total)


            img!!.apply {


                when (data!!.taskstate) {


                    PAUSE -> {
                        if (data!!.forcestop)
                            setImageResource(D.ic_arrow_upward_grey_400_24dp)
                        else
                            setImageResource(D.ic_pause_grey_400_24dp)
                    }


                    DONE -> {
                        setImageResource(D.ic_done_grey_400_24dp)
                        loadsize!!.text = SizeUtils.getSize(data?.total * data.filepersent / 100)
                    }
                    else -> {
                        if (data!!.forcestop)
                            setImageResource(D.ic_arrow_upward_grey_400_24dp)
                        else
                            setImageResource(D.ic_pause_grey_400_24dp)

                    }
                }
            }
        }
    }
}
