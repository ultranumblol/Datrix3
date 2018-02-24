package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/2/24.
 */

data class LoginInfo(

        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("result")
        @Expose
        val reuslt: result,

        @SerializedName("msg")
        @Expose
        val msg: String


) {

    data class result(

            @SerializedName("token")
            @Expose
            val token: String,

            @SerializedName("info")
            @Expose
            val info: info


    )

    data class info(
            @SerializedName("userid")
            @Expose
            val userid: String,

            @SerializedName("groupid")
            @Expose
            val groupid: String,

            @SerializedName("usernickname")
            @Expose
            val usernickname: String,

            @SerializedName("usertype")
            @Expose
            val usertype: String

    )


}