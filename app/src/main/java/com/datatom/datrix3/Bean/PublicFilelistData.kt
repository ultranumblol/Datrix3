package com.datatom.datrix3.Bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by wgz on 2018/2/24.
 */

data class PublicFilelistData(

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

            @SerializedName("dir")
            @Expose
            val dir: dir,

            @SerializedName("lin")
            @Expose
            val lin: lin


    ) : Serializable


    data class lin(
            @SerializedName("total")
            @Expose
            val total: Int,

            @SerializedName("result")
            @Expose
            val resultlin: List<result_lin>
    ) : Serializable


    data class result_lin(
            @SerializedName("total")
            @Expose
            val total: Int

    ) :Serializable


    data class dir(@SerializedName("total")
                   @Expose
                   val total: Int,

                   @SerializedName("result")
                   @Expose
                   val result2: List<PersonalFilelistData.result2>) : Serializable


}