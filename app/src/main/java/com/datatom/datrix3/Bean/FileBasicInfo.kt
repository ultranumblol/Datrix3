package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/3/15.
 */
data class FileBasicInfo (
        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("result")
        @Expose
        val reuslt: result,

        @SerializedName("msg")
        @Expose
        val msg: String

){


    data class  result (
            @SerializedName("objid")
            @Expose
            val objid: String,

            @SerializedName("filename")
            @Expose
            val filename: String,

            @SerializedName("filesize")
            @Expose
            val filesize: String,

            @SerializedName("fileid")
            @Expose
            val fileid: String,

            @SerializedName("parentid")
            @Expose
            val parentid: String,

            @SerializedName("createuid")
            @Expose
            val createuid: String,

            @SerializedName("createtime")
            @Expose
            val createtime: String,


            @SerializedName("fullpath")
            @Expose
            var fullpath : String,

            @SerializedName("modiftime")
            @Expose
            val modiftime: String,

            @SerializedName("gpscity")
            @Expose
            val gpscity: String,

            @SerializedName("gpsdistrict")
            @Expose
            val gpsdistrict: String,

            @SerializedName("state_view")
            @Expose
            val state_view: String,

            @SerializedName("state_dload")
            @Expose
            val state_dload: String,

            @SerializedName("state_fav")
            @Expose
            val state_fav: String,

            @SerializedName("state_share")
            @Expose
            val state_share: String,

            @SerializedName("groupnickname")
            @Expose
            val groupnickname: String,

            @SerializedName("groupid")
            @Expose
            val groupid: String,

            @SerializedName("userid")
            @Expose
            val userid: String

    )
}