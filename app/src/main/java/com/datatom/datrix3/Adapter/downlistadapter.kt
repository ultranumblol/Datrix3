package com.datatom.datrix3.Adapter;

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.datatom.datrix3.Bean.TaskFile
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Service.TaskService
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.Util.UploadFileUtil2
import com.datatom.datrix3.Util.UploadFileUtil2.Companion.PAUSE
import com.datatom.datrix3.helpers.D
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

                //                setOnClickListener {
//
//                    ("img click : "+data.toString()) .LogD()
//
//                    when (data.taskstate) {
//
//                        PAUSE -> {
//                        data.taskstate = WRITING
//                        AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(data)
//                        notifyDataSetChanged()
//                        UploadFileUtil2(data).ReUpload()
//
//                    }
//                    DONE -> {
//
//                        //todo 打开文件
//                    }
//                    else -> {
//
//                        data.taskstate = PAUSE
//                        AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(data)
//                        notifyDataSetChanged()
//                        RxApiManager().get()!!.cancel(data.id)
//
//                    }
//                    }
//
//                }

                when (data!!.taskstate) {
                    PAUSE -> {
                        setImageResource(D.ic_arrow_downward_grey_400_24dp)
                    }
                    TaskService.DONE -> {
                        setImageResource(D.ic_done_grey_400_24dp)
                        loadsize!!.text = SizeUtils.getSize(data?.total * data.filepersent / 100)
                    }
                    else -> {
                        setImageResource(D.ic_pause_grey_400_24dp)

                    }
                }
            }
        }

    }
}
