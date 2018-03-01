package com.datatom.datrix3.Util

/**
 * Created by wgz on 2017/4/28.
 */

object SizeUtils {
    fun getSize(str: String): String {
        //1M=1024*1024
        try {
            var size = java.lang.Long.valueOf(str)!!
            if (size > 1024 * 1024 * 1024) {
                size = size / 1024 / 1024
                val size_k = (size % 1024).toInt()
                val size_m = (size / 1024).toInt()
                val temp = size_k.toString()
                return size_m.toString() + "." + temp.substring(0, 1) + "G"
            }
            if (size > 1024 * 1024) {
                size = size / 1024
                val size_k = (size % 1024).toInt()
                val size_m = (size / 1024).toInt()
                val temp = size_k.toString()
                return size_m.toString() + "." + temp.substring(0, 1) + "M"
            } else if (size > 1024) {
                val size_B = (size % 1024).toInt()
                val size_k = (size / 1024).toInt()
                val temp = size_B.toString()
                return size_k.toString() + "." + temp.substring(0, 1) + "K"
            } else {
                return size.toString() + "B"
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }


        return ""
    }

    fun getSize(size: Long): String {
        var size = size
        //1M=1024*1024
        try {
            if (size > 1024 * 1024) {
                size = size / 1024
                val size_k = (size % 1024).toInt()
                val size_m = (size / 1024).toInt()
                val temp = size_k.toString()
                return size_m.toString() + "." + temp.substring(0, 1) + "M"
            } else if (size > 1024) {
                val size_B = (size % 1024).toInt()
                val size_k = (size / 1024).toInt()
                val temp = size_B.toString()
                return size_k.toString() + "." + temp.substring(0, 1) + "K"
            } else {
                return size.toString() + "B"
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }

        return ""
    }
}
