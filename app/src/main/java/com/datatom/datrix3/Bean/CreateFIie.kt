package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/3/1.
 */
data class CreateFIie(

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


            @SerializedName("objid")
            @Expose
            val objid: String,

            @SerializedName("filename")
            @Expose
            val filename: String,

            @SerializedName("fileid")
            @Expose
            val fileid: String,

            @SerializedName("dirpath")
            @Expose
            val dirpath: String

    )

}