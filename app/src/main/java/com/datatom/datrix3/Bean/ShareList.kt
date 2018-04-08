package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/4/4.
 */
data class ShareList(
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

    data class  result(

            @SerializedName("total")
            @Expose
            val total: Int,


            @SerializedName("hits")
            @Expose
            val hits: List<Hits>


    )

    data class Hits(
            @SerializedName("_index")
            @Expose
            val total: String,
            @SerializedName("_id")
            @Expose
            val _id: String,
            @SerializedName("_score")
            @Expose
            val _score: String,
            @SerializedName("_source")
            @Expose
            val _source: Source
            )

    data class  Source(

            @SerializedName("sharename")
            @Expose
            val sharename: String,
            @SerializedName("descript")
            @Expose
            val descript: String,
            @SerializedName("shareuid")
            @Expose
            val shareuid: String,
            @SerializedName("username")
            @Expose
            val username: String,
            @SerializedName("ctime")
            @Expose
            val ctime: String,
            @SerializedName("shareurl")
            @Expose
            val shareurl: String,
            @SerializedName("sharepwd")
            @Expose
            val sharepwd: String,
            @SerializedName("sid")
            @Expose
            val sid: String,
            @SerializedName("files")
            @Expose
            val files: List<Files>
    )

    data class Files(
            @SerializedName("fileid")
            @Expose
            val fileid: String,
            @SerializedName("filename")
            @Expose
            val filename: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("objid")
            @Expose
            val objid: String,
            @SerializedName("createuid")
            @Expose
            val createuid: String,
            @SerializedName("dirtype")
            @Expose
            val dirtype: String,

            @SerializedName("mimetype")
            @Expose
            val mimetype: String,
            @SerializedName("filesize")
            @Expose
            val filesize: String,
            @SerializedName("createtime")
            @Expose
            val createtime: String,

            @SerializedName("modiftime")
            @Expose
            val modiftime: String,
            @SerializedName("ext")
            @Expose
            val ext: String
    )

}