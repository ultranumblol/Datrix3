package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by wgz on 2018/2/24.
 */

data class SearchResultData(

        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("result")
        @Expose
        val reuslt: result,

        @SerializedName("msg")
        @Expose
        val msg: String


) : Serializable {

    data class result(

//            @SerializedName("dirs")
//            @Expose
//            val dirs: dirs,

            @SerializedName("files")
            @Expose
            val files:files


    ) : Serializable

        data class files(
                @SerializedName("total")
                @Expose
                val total: Int,

                @SerializedName("res")
                @Expose
                val res: List<res>

        ) :Serializable

//        data class dirs(
//                @SerializedName("total")
//                @Expose
//                val total: Int,
//
//                @SerializedName("res")
//                @Expose
//                val res: List<res>
//
//        ) :Serializable



    data class res(
            @SerializedName("userid")
            @Expose
            val userid: String,

            @SerializedName("prefix")
            @Expose
            val prefix: String,

            @SerializedName("name")
            @Expose
            val name: String,

            @SerializedName("bucket")
            @Expose
            val bucket: String,
            @SerializedName("size")
            @Expose
            val size: String,

            @SerializedName("ctime")
            @Expose
            val ctime: Int,

            @SerializedName("objid")
            @Expose
            val objid: String,

            @SerializedName("mtime")
            @Expose
            val mtime: Int,

            @SerializedName("acl")
            @Expose
            val acl: Int,
            @SerializedName("filename")
            @Expose
            val filename: String,

            @SerializedName("filesize")
            @Expose
            val filesize: String,

            @SerializedName("fileid")
            @Expose
            val fileid: String,

            @SerializedName("ext")
            @Expose
            val ext: String,


            @SerializedName("parentid")
            @Expose
            val parentid: String,

            @SerializedName("createuid")
            @Expose
            val createuid: String,

            @SerializedName("type")
            @Expose
            val type: String,

            @SerializedName("intrash")
            @Expose
            val intrash: String,

            @SerializedName("trashreason")
            @Expose
            val trashreason: String,
            @SerializedName("trashdate")
            @Expose
            val trashdate: String,

            @SerializedName("dirtype")
            @Expose
            val dirtype: String,

            @SerializedName("pdtype")
            @Expose
            val pdtype: String,

            @SerializedName("createtime")
            @Expose
            val createtime: String,
            @SerializedName("modiftime")
            @Expose
            val modiftime: String,

            @SerializedName("cayman_pretreat_mimetype")
            @Expose
            val cayman_pretreat_mimetype: String,

            @SerializedName("idpath")
            @Expose
            val idpath: String,
            @SerializedName("uploadok")
            @Expose
            val uploadok: String,

            @SerializedName("isinborn")
            @Expose
            val isinborn: String,

            @SerializedName("fullpath")
            @Expose
            val fullpath: String,
            @SerializedName("hasdflin")
            @Expose
            val hasdflin: String,

            @SerializedName("catalogstatus")
            @Expose
            val catalogstatus: String,

            @SerializedName("contentauditstatus")
            @Expose
            val contentauditstatus: String


    ) : Serializable


}