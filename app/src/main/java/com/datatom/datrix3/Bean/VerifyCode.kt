package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/2/27.
 */
data class VerifyCode(@SerializedName("code")
                      @Expose
                      val code: Int,
                      @SerializedName("result")
                      @Expose
                      val reuslt: String )