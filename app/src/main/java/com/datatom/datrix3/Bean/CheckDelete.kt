package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/4/18.
 */
data class CheckDelete(
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

            @SerializedName("hasacl")
            @Expose
            val hasacl: List<String>,


            @SerializedName("noacl")
            @Expose
            val noacl: List<String>
    )

}