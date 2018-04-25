package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/4/11.
 */
data class TrashBean(
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
            @SerializedName("total")
            @Expose
            val total: Int,


            @SerializedName("result")
            @Expose
            val result2: List<result2>

    )

    data class result2(

            @SerializedName("userid")
            @Expose
            val userid: String,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("objid")
            @Expose
            val objid: String,
            @SerializedName("size")
            @Expose
            val size: Int,

            @SerializedName("acl")
            @Expose
            val acl: String,
            @SerializedName("filename")
            @Expose
            val filename: String,
            @SerializedName("fileid")
            @Expose
            val fileid: String,
            @SerializedName("parentid")
            @Expose
            val parentid: String,
            @SerializedName("createuid")
            @Expose
            val createuid: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("ext")
            @Expose
            val ext: String,
            @SerializedName("intrash")
            @Expose
            val intrash: String,
            @SerializedName("trashreason")
            @Expose
            val trashreason: String,
            @SerializedName("trashdate")
            @Expose
            val trashdate: String,
            @SerializedName("idpath")
            @Expose
            val idpath: String,
            @SerializedName("cayman_pretreat_mimetype")
            @Expose
            val cayman_pretreat_mimetype: String,

            @SerializedName("uploadok")
            @Expose
            val uploadok: String,
            @SerializedName("filesize")
            @Expose
            val filesize: String,
            @SerializedName("applysub")
            @Expose
            val applysub: String,
            @SerializedName("seeus")
            @Expose
            val seeus: String,
            @SerializedName("isinborn")
            @Expose
            val isinborn: String

    )

}