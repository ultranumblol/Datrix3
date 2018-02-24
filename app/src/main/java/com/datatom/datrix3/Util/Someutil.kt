package com.datatom.datrix3.Util

import com.datatom.datrix3.app
import com.datatom.datrix3.base.AppConstant

/**
 * Created by wgz on 2018/2/24.
 *
 */

object Someutil {

    fun getUserID() : String{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_ID, "000") as String;

    }

    fun getUserNickname() : String{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_NICKNAME, "000") as String;

    }

    fun getToken() : String{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_TOKEN, "X7yABwjE20sUJLefATUFqU0iUs8mJPqEJo6iRnV63mI=") as String;

    }

    fun getAutologin() : Boolean{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.AUTO_LOGIN, false) as Boolean;

    }

    fun getrememberuser() : Boolean{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.REMEMBER_NAME_CODE, false) as Boolean;

    }

    fun getloginIP() : String{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_LOGINIP, "000") as String;

    }
    fun getloginname() : String{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_LOGINNAME, "000") as String;

    }
    fun getloginpwd() : String{

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_LOGINPASSWORD, "000") as String;

    }
}

