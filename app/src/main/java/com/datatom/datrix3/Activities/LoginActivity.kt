package com.datatom.datrix3.Activities


import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.R
import com.githang.statusbar.StatusBarCompat
import com.jakewharton.rxbinding2.view.RxView


import kotlinx.android.synthetic.main.activity_login.*

import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * 登录页面
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        StatusBarCompat.setStatusBarColor(this,resources.getColor(R.color.login_bg))

        addUsernameAutoComplete()
        RxView.clicks(button_get_started).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe{
                    dologin();
                }



    }

    private fun addUsernameAutoComplete() {

        //系统读入内容帮助用户输入用户名
        val arrayList = ArrayList<String>()
        /* for (int i = 0; i < 9; i++) {
            arrayList.add("03028"+i);
        }*/

        arrayList.add("10.0.10.7:18")
        val adapter = ArrayAdapter(application,
                R.layout.simple_dropdown_item_1line, arrayList)
        actv_ip.setThreshold(1)
        actv_ip.setAdapter(adapter)

    }

    private fun dologin() {


    }


}
