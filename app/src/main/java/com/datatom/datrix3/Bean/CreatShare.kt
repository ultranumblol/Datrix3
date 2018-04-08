package com.datatom.datrix3.Bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wgz on 2018/4/4.
 *
 * {"code":200,"result":{"shareid":"9d257e6536180641c6f4a2c5ddf933aa","sharename":"sharefile","descript":"123","shareuid":"test","username":"\u6d4b\u8bd5\u7528\u6237","ctime":"2018-04-05 00:11:45","shcandown":"1","shcanedit":"0","istimer":"0","edtime":9999999999,"pubsee":"0","users":[],"groups":[],"shareurl":"\/shares?shareid=9d257e6536180641c6f4a2c5ddf933aa","needpwd":"1","sharepwd":"jlcp","files":[{"fileid":"76e037da280dc0ec12aac28eb765d71d.wmv","filename":"\u91ce\u751f\u52a8\u7269.wmv","type":"1","objid":"20180319\/17\/76e037da280dc0ec12aac28eb765d71d.wmv","createuid":"test","dirtype":"-1","mimetype":"video","filesize":"26246026","createtime":"2018-03-19 17:42:08","modiftime":"2018-03-19 17:42:09","ext":"wmv"},{"fileid":"f7b755f30a8a41bf040ff2ef76816458.jpg","filename":"Chrysanthemum.jpg","type":"1","objid":"20180319\/17\/f7b755f30a8a41bf040ff2ef76816458.jpg","createuid":"test","dirtype":"-1","mimetype":"image","filesize":"879394","createtime":"2018-03-19 17:38:19","modiftime":"2018-03-19 17:38:20","ext":"jpg"}],"sid":"N2RkY2FjNjNiYTM0YjVhNDNhYjc2OTE3NmU0YjlhZWQsMywy","downnum":0,"readnum":0}}

 *
 *
 *
 */
data class CreatShare(
        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("result")
        @Expose
        val result: reuslt,

        @SerializedName("msg")
        @Expose
        val msg: String

) {

    data class reuslt(

            @SerializedName("shareurl")
            @Expose
            val shareurl: String,

            @SerializedName("sharepwd")
            @Expose
            val sharepwd: String

    )

}