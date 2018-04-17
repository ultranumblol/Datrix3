package com.datatom.datrix3.Adapter;

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.Activities.SharedFilesActivity
import com.datatom.datrix3.Bean.ShareList
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.RxBus
import com.datatom.datrix3.helpers.createQRImage
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

/**
 * Created by wgz
 */
class openshareadapter(context: Context) : RecyclerArrayAdapter<ShareList.Hits>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return openshareadapterViewholder(parent)
    }

    private inner class openshareadapterViewholder(itemView: ViewGroup) : BaseViewHolder<ShareList.Hits>(itemView, R.layout.item_openshareadapter) {
        private var img_download: ImageView? = null
        private var img_delete: ImageView? = null
        private var img_code: ImageView? = null

        private var whosharefile: TextView? = null
        private var sharetime: TextView? = null
        private var rv: EasyRecyclerView? = null
        private var rvadapter: ShareRvAdapter

        init {

            img_download = `$`(R.id.img_share_download)
            img_delete = `$`(R.id.img_share_delete)
            img_code = `$`(R.id.img_share_code)

            whosharefile = `$`(I.tv_who_share_file)
            sharetime = `$`(I.tv_sharetime)
            rv = `$`(I.rv_sharefile)

            rvadapter = ShareRvAdapter(context)
            rv!!.apply {
                isNestedScrollingEnabled = true
                setLayoutManager(LinearLayoutManager(context))
                adapter = rvadapter
            }


            //img = `$`<ImageView>(R.id.item_img)

            // text = `$`<TextView>(R.id.item_text)
        }

        override fun setData(data: ShareList.Hits?) {
            rvadapter.clear()
            rvadapter.addAll(data!!._source.files)
            sharetime!!.text = data!!._source.ctime
            whosharefile!!.text = data._source.shareuid + "分享了一个文件"

            rvadapter.setOnItemClickListener {
                context.startActivity(Intent(context, SharedFilesActivity::class.java).putExtra("files",data._source))

            }

            img_download!!.apply {
                setOnClickListener {


                }

            }
            img_delete!!.apply {
                setOnClickListener {
                    AlertDialog.Builder(context)
                            .run {
                                setTitle("取消分享")
                                setMessage("确定删除该分享文件？")
                                setPositiveButton("确定", { _, _ ->
                                    HttpUtil.instance.apiService()
                                            .linkdelete(Someutil.getToken(), data._source.shareid)
                                            .compose(RxSchedulers.compose())
                                            .subscribe({
                                                if (it.contains("200")) {
                                                    context.toast("删除成功！")
                                                    handler.postDelayed({ RxBus.get().post("refresh_share") }, 1000)


                                                } else {
                                                    context.toast("删除失败！")
                                                }
                                            }, {
                                                it.toString().LogD("error : ")
                                                context.toast("删除失败！")
                                            })
                                })
                                setNegativeButton("取消", { _, _ ->

                                })
                                show()

                            }

                }

            }
            img_code!!.apply {
                setOnClickListener {

                    var qrimage = View.inflate(context, R.layout.dialog_qr_image, null)
                    var image = qrimage.find<ImageView>(I.dialog_image)
                    image.createQRImage(HttpUtil.BASEAPI_URL +"share.html#!"+data!!._source.shareurl)

                    android.support.v7.app.AlertDialog.Builder(context).apply {
                        setView(qrimage)
                        show()
                    }

                }

            }


        }

    }
}
