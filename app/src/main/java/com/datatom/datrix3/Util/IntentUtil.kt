package com.datatom.datrix3.Util

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import java.io.File
import android.support.v4.content.ContextCompat.startActivity




/**
 * Created by wgz on 2018/3/5.
 */
object IntentUtil {


    fun textopen(param: String , type :String) : Intent{

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//officeFile：本地文档；type：文档MIMEType类型，可以使用文件格式后缀
        intent.setDataAndType(Uri.fromFile(File(param)), type)
       return  intent

    }

    //Android获取一个用于打开PPT文件的intent
    fun getPptFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        return intent
    }

    //Android获取一个用于打开Excel文件的intent
    fun getExcelFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        return intent
    }

    //Android获取一个用于打开Word文件的intent
    fun getWordFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/msword")
        return intent
    }

    //Android获取一个用于打开CHM文件的intent
    fun getChmFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/x-chm")
        return intent
    }

    //Android获取一个用于打开文本文件的intent
    fun getTextFileIntent(param: String, paramBoolean: Boolean): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (paramBoolean) {
            val uri1 = Uri.parse(param)
            intent.setDataAndType(uri1, "text/plain")
        } else {
            val uri2 = Uri.fromFile(File(param))
            intent.setDataAndType(uri2, "text/plain")
        }
        return intent
    }

    //Android获取一个用于打开PDF文件的intent
    fun getPdfFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/pdf")
        return intent
    }
}