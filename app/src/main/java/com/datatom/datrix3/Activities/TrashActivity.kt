package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import collections.forEach
import com.datatom.datrix3.Adapter.trashadapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.*
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.activity_trash.*
import org.jetbrains.anko.toast


class TrashActivity : BaseActivity() {


    private lateinit var rvadapter: trashadapter
    private var page = 1
    private var showselectall = false

    override fun ActivityLayout(): Int {

        return R.layout.activity_trash
    }

    override fun initView() {

        setToolbartitle("回收箱")
        rvadapter = trashadapter(this)

        rv_trash.apply {
            setLayoutManager(LinearLayoutManager(this@TrashActivity))
            adapter = rvadapter
        }
        trash_probar.hide()
        initdata()



        btn_clear_trash.setOnClickListener {
            AlertDialog.Builder(this)
                    .apply {
                        setTitle("是否清空回收站？")
                        setPositiveButton("确定", { _, _ ->
                            trash_probar.Show()
                            HttpUtil.instance.apiService().trashclean(Someutil.getToken(), Someutil.getUserID())
                                    .compose(RxSchedulers.compose())
                                    .subscribe({
                                        if (it.contains("200")) {
                                            Handler().postDelayed({
                                                initdata()
                                                this@TrashActivity.toast("清理完成！")
                                                trash_probar.hide()
                                            }, 1000)

                                        }
                                    }, {
                                        this@TrashActivity.toast("请求错误！")
                                        trash_probar.hide()
                                    })

                        })
                        setNegativeButton("取消", { _, _ -> })
                        show()
                    }

        }

        RxBus.get().toFlowable(String::class.java).subscribe {
            when (it) {
                "updatetrashcheckbox" -> {
                    updatecheckBox()

                }
            }
        }


    }

    private fun updatecheckBox() {

        if (rvadapter!!.getcheckboxArrary().size() > 0) {


            showselectall = true
            invalidateOptionsMenu()
            rvadapter!!.notifyDataSetChanged()


        } else {
            showselectall = false
            invalidateOptionsMenu()
            rvadapter!!.notifyDataSetChanged()
        }


    }

    private fun initdata() {

        HttpUtil.instance.apiService().trash_list(Someutil.getToken(), Someutil.getUserID(), page, 50)
                .compose(RxSchedulers.compose())
                .subscribe({
                    it.toString().LogD("result : ")
                    rvadapter.clear()
                    rvadapter.addAll(it.reuslt.result2)

                }, {

                })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.apply {
            findItem(R.id.action_allselect).isVisible = showselectall
            findItem(I.action_huifu).isVisible = showselectall
            findItem(I.action_cddelete).isVisible = showselectall
        }
        return super.onPrepareOptionsMenu(menu)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.trash_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_allselect) {
            rvadapter!!.apply {
                if (rvadapter!!.getcheckboxArrary().size() > 0 && rvadapter!!.getcheckboxArrary().size() == rvadapter!!.count) {
                    setCheckBoxNoneSelect()
                    notifyDataSetChanged()
                    RxBus.get().post("updatetrashcheckbox")
                } else {
                    setCheckBoxAllSelect()
                    notifyDataSetChanged()
                    RxBus.get().post("updatetrashcheckbox")
                }
            }


        }
        if (id == R.id.action_huifu) {

            AlertDialog.Builder(this)
                    .apply {
                        setTitle("确定要还原所选文件吗？")
                        setPositiveButton("确定", { _, _ ->
                            trash_probar.Show()
                            rvadapter.apply {
                                var fileidarr = arrayListOf<String>()
                                var createuidarr = arrayListOf<String>()
                                var objidarr = arrayListOf<String>()
                                var parentidarr = arrayListOf<String>()

                                getcheckboxArrary().forEach { i, b ->
                                    fileidarr.add(allData[i].fileid)
                                    createuidarr.add(allData[i].createuid)
                                    objidarr.add(allData[i].objid)
                                    parentidarr.add(allData[i].parentid)


                                }
                                fileidarr.toString().LogD("fileidarr : ")
                                HttpUtil.instance.apiService().trashundo(Someutil.getToken(), fileidarr
                                        , createuidarr, objidarr, parentidarr)
                                        .compose(RxSchedulers.compose())
                                        .subscribe({
                                            it.LogD("result : ")
                                            if (it.contains("200")) {

                                                Handler().postDelayed({
                                                    rvadapter!!.setCheckBoxNoneSelect()
                                                    RxBus.get().post("updatetrashcheckbox")
                                                    initdata()
                                                    RxBus.get().post("refreshSpacelist")
                                                    trash_probar.hide()
                                                }, 1000)

                                            } else {
                                                trash_probar.hide()

                                                this@TrashActivity.toast("文件还原失败")
                                            }

                                        }, {
                                            trash_probar.hide()
                                            this@TrashActivity.toast("文件还原失败")
                                        })


                            }


                        })
                        setNegativeButton("取消", { _, _ -> })
                        show()
                    }

        }
        if (id == R.id.action_cddelete) {
            AlertDialog.Builder(this)
                    .apply {
                        setTitle("确定要删除所选文件吗？")
                        setPositiveButton("确定", { _, _ ->
                            trash_probar.Show()
                            rvadapter.apply {
                                var fileidarr = arrayListOf<String>()
                                var createuidarr = arrayListOf<String>()
                                var objidarr = arrayListOf<String>()
                                var parentidarr = arrayListOf<String>()

                                getcheckboxArrary().forEach { i, b ->
                                    fileidarr.add(allData[i].fileid)
                                    createuidarr.add(allData[i].createuid)
                                    objidarr.add(allData[i].objid)
                                    parentidarr.add(allData[i].parentid)


                                }
                                HttpUtil.instance.apiService().trashdelete(Someutil.getToken(), fileidarr, createuidarr
                                        , objidarr)
                                        .compose(RxSchedulers.compose())
                                        .subscribe({
                                            it.LogD("result : ")
                                            if (it.contains("200")) {
                                                Handler().postDelayed({
                                                    rvadapter!!.setCheckBoxNoneSelect()
                                                    RxBus.get().post("updatetrashcheckbox")
                                                    RxBus.get().post("refreshSpacelist")

                                                    initdata()
                                                    trash_probar.hide()
                                                }, 1000)


                                            } else {
                                                trash_probar.hide()

                                                this@TrashActivity.toast("文件删除失败")
                                            }

                                        }, {
                                            trash_probar.hide()
                                            this@TrashActivity.toast("文件删除失败")
                                        })


                            }

                        })
                        setNegativeButton("取消", { _, _ -> })
                        show()
                    }

        }

        return super.onOptionsItemSelected(item)
    }

}
