package com.datatom.datrix3.Activities


import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AlertDialog
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.hide
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.activity_saoma.*
import org.jetbrains.anko.find

import java.util.*


class SaomaActivity : BaseActivity() {

    private var beepManager: BeepManager? = null
    private var resultString: String? = null

    override fun ActivityLayout(): Int {

        return R.layout.activity_saoma
    }

    override fun initView() {
        toolBar!!.hide()
        backimg.setOnClickListener {
            onBackPressed()
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        val formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcode_scanner.getBarcodeView().setDecoderFactory(DefaultDecoderFactory(formats))
        barcode_scanner.decodeContinuous(callback)

        beepManager = BeepManager(this)
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == resultString) {
                // Prevent duplicate scans
                return
            }

            resultString = result.text
            barcode_scanner.setStatusText(result.text)

            beepManager!!.playBeepSoundAndVibrate()
            if (resultString!!.contains("datrix3/request.html")) {
                AlertDialog.Builder(this@SaomaActivity).run {
                    setTitle("此链接需要在浏览器打开，点击继续跳转")
                    setMessage(resultString)
                    setPositiveButton("继续", { _, _ ->
                        var url = "http://192.168.3.217/datrix3/request.html?sid=YWM2ZDBiNmM0M2Q3MWIzYTQxOGFlNjBmZDVhNDRiOTYsNSw1&requestid=1a56435e3bc342d4e432cf8b75ad0f00"
                        var intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        var content_url = Uri.parse(resultString)
                        intent.data = content_url
                        startActivity(Intent.createChooser(intent, "请选择浏览器"))
                    })
                    setNegativeButton("取消", { _, _ -> })
                    setOnDismissListener{
                        this@SaomaActivity.finish()
                    }
                    show()
                }


            } else {
                var dirlayout = View.inflate(this@SaomaActivity, R.layout.dialog_saoyisao_result, null)
                var rv = dirlayout.find<TextView>(I.dialog_result)
                rv.text = resultString
                rv.autoLinkMask = Linkify.WEB_URLS
                rv.movementMethod = LinkMovementMethod.getInstance()


                AlertDialog.Builder(this@SaomaActivity).run {
                    setTitle("扫码结果：")
                    setView(dirlayout)
                    setOnDismissListener {
                        this@SaomaActivity.finish()
                    }

                    show()

                }
            }

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onResume() {
        super.onResume()
        barcode_scanner.resume()
    }

    override fun onPause() {
        super.onPause()
        barcode_scanner.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcode_scanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

}
