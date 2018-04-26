package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by wgz on 2018/4/26.
 */
data class CollectFiles(
        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("result")
        @Expose
        val reuslt: result,

        @SerializedName("msg")
        @Expose
        val msg: String)  : Serializable{

    data class result(
            @SerializedName("took")
            @Expose
            val took: Int,
            @SerializedName("timed_out")
            @Expose
            val timed_out: Boolean,
            @SerializedName("_shards")
            @Expose
            val _shards: shards,
            @SerializedName("hits")
            @Expose
            val hits1: hits,
            @SerializedName("aggregations")
            @Expose
            val aggregations_: aggregations): Serializable {

        data class shards(

                @SerializedName("total")
                @Expose
                val total: Int,
                @SerializedName("successful")
                @Expose
                val successful: Int,
                @SerializedName("failed")
                @Expose
                val failed: Int): Serializable

        data class hits(
                @SerializedName("total")
                @Expose
                val total_hits1: Int,

                @SerializedName("max_score")
                @Expose
                val max_score: Int,
                @SerializedName("hits")
                @Expose
                val hits2s: List<hits2>): Serializable {

            data class hits2(

                    @SerializedName("_index")
                    @Expose
                    val _index: String,
                    @SerializedName("_type")
                    @Expose
                    val _type: String,
                    @SerializedName("_id")
                    @Expose
                    val _id: String,
                    @SerializedName("_score")
                    @Expose
                    val _score: String,
                    @SerializedName("_source")
                    @Expose
                    val _source: source): Serializable {
                data class source(

                        @SerializedName("collectid")
                        @Expose
                        val collectid: String,
                        @SerializedName("ob jectcollectid")
                        @Expose
                        val ob_jectcollectid: String,
                        @SerializedName("createuid")
                        @Expose
                        val createuid: String,
                        @SerializedName("objid")
                        @Expose
                        val objid: String,
                        @SerializedName("fileid")
                        @Expose
                        val fileid: String,
                        @SerializedName("from")
                        @Expose
                        val from: String,
                        @SerializedName("fromid")
                        @Expose
                        val fromid: String,
                        @SerializedName("intrash")
                        @Expose
                        val intrash: Boolean,
                        @SerializedName("timeout")
                        @Expose
                        val timeout: Boolean,
                        @SerializedName("createtime")
                        @Expose
                        val createtime: String,
                        @SerializedName("cayman_pretreat_mimetype")
                        @Expose
                        val cayman_pretreat_mimetype: String,
                        @SerializedName("parentid")
                        @Expose
                        val parentid: String,
                        @SerializedName("filename")
                        @Expose
                        val filename: String,
                        @SerializedName("type")
                        @Expose
                        val type: String,
                        @SerializedName("dirtype")
                        @Expose
                        val dirtype: String,
                        @SerializedName("size")
                        @Expose
                        val size: String,
                        @SerializedName("ext")
                        @Expose
                        val ext: String,
                        @SerializedName("tags")
                        @Expose
                        val tags: List<String>): Serializable
            }

        }


        data class aggregations(
                @SerializedName("tgs")
                @Expose
                val tgs_: tgs) : Serializable{
            data class tgs(
                    @SerializedName("doc_count_error_upper_bound")
                    @Expose
                    val doc_count_error_upper_bound: Int,
                    @SerializedName("sum_other_doc_count")
                    @Expose
                    val sum_other_doc_count: Int,
                    @SerializedName("buckets")
                    @Expose
                    val buckets_: List<buckets>) : Serializable{
                data class buckets(
                        @SerializedName("key")
                        @Expose
                        val key: String,
                        @SerializedName("doc_count")
                        @Expose
                        val doc_count: Int): Serializable

            }


        }

    }


}