package com.datatom.datrix3.Activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils

import android.widget.ArrayAdapter
import android.widget.Button
import com.datatom.datrix3.Bean.LoginInfo

import com.datatom.datrix3.R

import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.SPBuild
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.Someutil.updateToken
import com.datatom.datrix3.app
import com.datatom.datrix3.base.AppConstant
import com.datatom.datrix3.helpers.*

import com.githang.statusbar.StatusBarCompat
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.schedulers.Schedulers


import kotlinx.android.synthetic.main.activity_login.*

import org.jetbrains.anko.find
import org.jetbrains.anko.toast

import java.util.ArrayList
import java.util.concurrent.TimeUnit


/**
 * 登录页面
 */
class LoginActivity : AppCompatActivity() {

    var mbutton: Button? = null
    private var ifupdate  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.login_bg))

        mbutton = find(R.id.button_get_started)


        tv_saoyisao.setOnClickListener {
            Someutil.checkPermissionCAMERA(this)
            this.startActivity(Intent(this,SaoyisaoActivity::class.java))

        }
        //addUsernameAutoComplete()
        RxView.clicks(mbutton!!).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe {
                    dologin();
                }

        initData()
    }

    private fun initData() {

        if (Someutil.getAutologin()){

            cb_auto_login.isChecked = true


            if (System.currentTimeMillis() - Someutil.getlastLogintime() > AppConstant.TOKEN_LASTTIME){

                    ifupdate = true
                startActivity(Intent(this@LoginActivity, MainActivity::class.java).putExtra("ifupdate",ifupdate))
                this@LoginActivity.finish()

            }
            else{
                ifupdate = false
                startActivity(Intent(this@LoginActivity, MainActivity::class.java).putExtra("ifupdate",ifupdate))
                this@LoginActivity.finish()
            }

        }
        if (Someutil.getrememberuser()){

            cb_rem_user.isChecked = true
            actv_ip.setText(Someutil.getloginIP())
            actv_username.setText(Someutil.getloginname())
            edit_password.setText(Someutil.getloginpwd())

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





        if (actv_ip.text.toString().isEmpty()){

            login_rootview.ShowSnackbarshort("请输入ip")
            return
        }

//        if (!actv_ip.text.toString().IsIP()){
//            login_rootview.ShowSnackbarshort("请输入正确的ip格式")
//            return
//
//        }

        var name = actv_username.text.toString()

        if (TextUtils.isEmpty(name)){
            login_rootview.ShowSnackbarshort("请输入用户名")
            return

        }

        var pwd = if (actv_username.text.toString().contains("\\")) edit_password.text.toString().AES()
        else edit_password.text.toString().MD5()

        if (TextUtils.isEmpty(pwd)){
            login_rootview.ShowSnackbarshort("请输入密码")
            return

        }

        login_wait.Show()
        SPBuild(applicationContext)
                .addData(AppConstant.USER_LOGINIP,actv_ip.text.toString())
                .build()

        HttpUtil.BASE_URL = "http://${Someutil.getloginIP()}/api/sw/"
        HttpUtil.BASEAPI_URL = "http://${Someutil.getloginIP()}/"

        HttpUtil.BASE_URL.LogD(" httpurl : ")

        HttpUtil.instance.apiService().login(name, pwd, "uname", "android")
                .compose(RxSchedulers.compose())
                .subscribe({
                    ("login result :" + it.toString()).LogD()

                    when (it.code) {

                        200 -> {
                            Saveinfo(it)
                            login_wait.hide()
                            login_rootview.ShowSnackbarshort("登录成功！")
                                    .addCallback(object : Snackbar.Callback() {
                                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                            this@LoginActivity.startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                            this@LoginActivity.finish()
                                        }
                                    })

                        }
                        else -> {
                            login_wait.hide()
                            login_rootview.ShowSnackbarshort("登录失败，请重试")

                        }
                    }


                }, {
                    ("error : " + it.toString()).LogD()
                    login_wait.hide()
                    login_rootview.ShowSnackbarshort("登录失败，请重试")


                })

    }

    /**
     * 保存用户信息只在登录成功时候保存
     */
    private fun Saveinfo(info: LoginInfo?) {
            //保存checkbox 勾选状态
            SPBuild(applicationContext)
                    .addData(AppConstant.AUTO_LOGIN,cb_auto_login.isChecked)
                    .addData(AppConstant.REMEMBER_NAME_CODE,cb_rem_user.isChecked)
                    .build()

            if (cb_rem_user.isChecked) {
                //保存登录密码用户名ip
                SPBuild(applicationContext)
                        .addData(AppConstant.USER_LOGINIP,actv_ip.text.toString())
                        .addData(AppConstant.USER_LOGINNAME,actv_username.text.toString())
                        .addData(AppConstant.USER_LOGINPASSWORD,edit_password.text.toString())
                        .build()

            }else{
                SPBuild(applicationContext)
                        .addData(AppConstant.USER_LOGINIP,Someutil.getloginIP())
                        .addData(AppConstant.USER_LOGINNAME,"")
                        .addData(AppConstant.USER_LOGINPASSWORD,"")
                        .build()


            }


        info!!.let {
                //保存返回的用户id token

                SPBuild(applicationContext)
                        .addData(AppConstant.USER_ID,it.reuslt.info.userid)
                        .addData(AppConstant.USER_NICKNAME,it.reuslt.info.usernickname)
                        .addData(AppConstant.USER_TOKEN,it.reuslt.token)
                        .addData(AppConstant.LOGIN_TIME,System.currentTimeMillis())
                        .build()

            }




    }


}
