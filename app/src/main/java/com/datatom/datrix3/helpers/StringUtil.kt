package com.datatom.datrix3.helpers

import android.content.ClipData
import android.provider.SyncStateContract.Helpers.update
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and
import android.text.TextUtils
import android.util.Base64
import java.util.regex.Pattern

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.datatom.datrix3.app


/**
 * Created by wgz on 2018/2/24.
 */
fun String.MD5() : String{

    if (TextUtils.isEmpty(this)) {
        return ""
    }

    try {
        val instance:MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest:ByteArray = instance.digest(this.toByteArray())//对字符串加密，返回字节数组
        var sb : StringBuffer = StringBuffer()
        for (b in digest) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""


}

fun String.IsIP() : Boolean{
    if (this.length < 7 || this.length > 15 || "" == this) {
        return false
    }
    /**
     * 判断IP格式和范围
     */
    val rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"

    val pat = Pattern.compile(rexp)

    val mat = pat.matcher(this)

    return mat.find()



}


fun String.AES() : String {
    //创建cipher对象
    val cipher = Cipher.getInstance("AES")
    //初始化cipher
    //通过秘钥工厂生产秘钥
    val keySpec  = SecretKeySpec("www.datatom.com".MD5().toByteArray(), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    //加密、解密
    val encrypt = cipher.doFinal(this.toByteArray())
    return Base64.encodeToString(encrypt,Base64.DEFAULT)
//    return Base64.encode(encrypt,Base64.D.EFAULT).toString()


}


fun String.LogD(str : String = ""){

    android.util.Log.d("wgz",str + this)

}

fun String.copy(){
    //获取剪贴板管理器：
    val cm = app.Companion.mapp.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
// 创建普通字符型ClipData
    val mClipData = ClipData.newPlainText("Label", this)
// 将ClipData内容放到系统剪贴板里。
    cm.setPrimaryClip(mClipData)

}

