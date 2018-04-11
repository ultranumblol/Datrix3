package com.datatom.datrix3.helpers

import android.graphics.Bitmap
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*

/**
 * Created by wgz on 2017/11/2.
 */
fun View.Show() {
    visibility = View.VISIBLE

}

fun View.hide() {

    visibility = View.GONE


}

fun View.inVisible() {

    visibility = View.INVISIBLE


}


fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun View.ShowSnackbarLong(str: String): Snackbar {

    val snackbar = Snackbar.make(this, str, Snackbar.LENGTH_LONG)

    snackbar.show()

    return snackbar

}

fun View.ShowSnackbarshort(str: String): Snackbar {

    val snackbar = Snackbar.make(this, str, Snackbar.LENGTH_SHORT)

    snackbar.show()

    return snackbar

}

fun Snackbar.showLong(string: String, root: View) {
    Snackbar.make(root, string, Snackbar.LENGTH_LONG).show()

}

fun ImageView.createQRImage(url : String){
    try {
        //判断URL合法性
        if (url == null || "" == url || url.length < 1) {
            return
        }
        val hints = Hashtable<EncodeHintType, String>()
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
        //图像数据转换，使用了矩阵转换
        val bitMatrix = QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, 280, 280, hints)
        val pixels = IntArray(280 * 280)
        //下面这里按照二维码的算法，逐个生成二维码的图片，
        //两个for循环是图片横列扫描的结果
        for (y in 0 until 280) {
            for (x in 0 until 280) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * 280 + x] = -0x1000000
                } else {
                    pixels[y * 280 + x] = -0x1
                }
            }
        }
        //生成二维码图片的格式，使用ARGB_8888
        val bitmap = Bitmap.createBitmap(280, 280, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, 280, 0, 0, 280, 280)
        //显示到一个ImageView上面
        this.setImageBitmap(bitmap)

    } catch (e: WriterException) {
        e.printStackTrace()
    }

}