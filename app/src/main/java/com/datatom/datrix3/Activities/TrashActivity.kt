package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.datatom.datrix3.Adapter.trashadapter
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.LogD
import kotlinx.android.synthetic.main.activity_trash.*


class TrashActivity : BaseActivity() {


    private lateinit var rvadapter : trashadapter
    private var page  = 1

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

        initdata()





    }

    private fun initdata() {
        HttpUtil.instance.apiService().trash_list(Someutil.getToken(),Someutil.getUserID(),page,50)
                .compose(RxSchedulers.compose())
                .subscribe({
                    it.toString().LogD("result : ")
                    rvadapter.addAll(it.reuslt.result2)

                },{

                })
    }
}
