package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import kotlinx.android.synthetic.main.activity_file_collect_web_view.*


class FileCollectWebViewActivity : BaseActivity() {


    override fun ActivityLayout(): Int {

        return R.layout.activity_file_collect_web_view
    }

    override fun initView() {
        setToolbartitle("文件收集")

        val settings = web_view.settings
        //settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.defaultTextEncodingName = "UTF-8"
       // settings.blockNetworkImage = false
        settings.javaScriptEnabled = true
        //settings.domStorageEnabled = true
        //settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webView: WebView?, request: WebResourceRequest?): Boolean {
                webView?.loadUrl(request?.url.toString())
                return true
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                view?.loadUrl("file:///android_asset/page404.html")
            }
        }

        web_view.loadUrl("http://192.168.3.217/datrix3/request.html?sid=YWM2ZDBiNmM0M2Q3MWIzYTQxOGFlNjBmZDVhNDRiOTYsNSw1&requestid=1a56435e3bc342d4e432cf8b75ad0f00")
    }
}
