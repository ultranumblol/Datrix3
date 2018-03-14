package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/3/12.
 */
data class Quota(
        @SerializedName("code")
        @Expose
        val code: Int,


        @SerializedName("result")
        @Expose
        val res: result,

        @SerializedName("msg")
        @Expose
        val msg: String

) {


    data class result(
            @SerializedName("userid")
            @Expose
            val userid: String,

            @SerializedName("quota")
            @Expose
            val quota: String,

            @SerializedName("used")
            @Expose
            val used: String

    )

}