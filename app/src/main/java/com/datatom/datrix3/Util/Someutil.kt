package com.datatom.datrix3.Util

import android.Manifest
import android.os.Environment
import com.datatom.datrix3.app
import com.datatom.datrix3.base.AppConstant
import com.datatom.datrix3.helpers.AES
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.MD5
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*
import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Created by wgz on 2018/2/24.
 *
 */

object Someutil {

    fun getUserID(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_ID, "000") as String;

    }

    fun getUserNickname(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_NICKNAME, "000") as String;

    }


    fun getToken(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_TOKEN, "X7yABwjE20sUJLefATUFqU0iUs8mJPqEJo6iRnV63mI=") as String;

    }

    fun getAutologin(): Boolean {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.AUTO_LOGIN, false) as Boolean;

    }

    fun getrememberuser(): Boolean {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.REMEMBER_NAME_CODE, false) as Boolean;

    }

    fun getloginIP(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_LOGINIP, "192.168.3.217") as String;

    }

    fun getloginname(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_LOGINNAME, "000") as String;

    }

    fun getloginpwd(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.USER_LOGINPASSWORD, "000") as String;

    }

    fun getDownloaddirs(): String {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.DOWNLOADDIRS, Environment.getExternalStorageDirectory().toString() + File.separator + "datrixdownload") as String;

    }

    fun getlastLogintime(): Long {

        return SPUtils.get(app.mapp.applicationContext, AppConstant.LOGIN_TIME, 0L) as Long

    }

    /**
     * 获取Glide造成的缓存大小 datrixdownloadTemp
     *
     * @return CacheSize
     */
    fun getCacheSize(): String {
        try {
            getFolderSize(File("${app.mapp.cacheDir}/IMG_CACHE")).toString().LogD("img : ")
            getFolderSize(File("${Environment.getExternalStorageDirectory()}/officetemp")).toString().LogD("officetmp : ")
            getFolderSize(File("${Environment.getExternalStorageDirectory()}/datrixdownloadTemp")).toString().LogD("download : ")

            return SizeUtils.getSize(getFolderSize(File("${app.mapp.cacheDir}/IMG_CACHE")) + getFolderSize(File("${Environment.getExternalStorageDirectory()}/officetemp"))+getFolderSize(File("${Environment.getExternalStorageDirectory()}/datrixdownloadTemp")))
        } catch (e: Exception) {
            e.printStackTrace()
            e.toString().LogD("cache error : ")
        }

        return ""
    }

    @JvmStatic
    fun dirSize(dir: File?): Long {
        dir?.let {
            var result = 0L
            if (dir.exists()) {
                val fileArray = dir.listFiles()
                for (file in fileArray) {
                    result += if (file.isDirectory) {
                        dirSize(file)
                    } else {
                        file.length()
                    }
                }
                return result / 1024 / 1024
            }
            return 0
        } ?: run {
            return 0L
        }
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                if (aFileList.isDirectory) {
                    size += getFolderSize(aFileList)
                } else {
                    size += aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    fun updateToken() {

        HttpUtil.instance.apiService().login(Someutil.getloginname(), if (Someutil.getloginname().contains("\\")) Someutil.getloginpwd().AES() else Someutil.getloginpwd().MD5(), "uname", "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe({
                    SPBuild(app.mapp.applicationContext)
                            .addData(AppConstant.USER_TOKEN, it.reuslt.token)
                            .build()


                }, {
                    it.toString().LogD("updatetoken error : ")
                    //Thread.sleep(2000)
                    // updateToken()


                })


    }

    fun doOpenWord(filetype: String, context: Context, file: File): String {

        var intent = Intent()

        intent.setAction("android.intent.action.VIEW")

        intent.addCategory("android.intent.category.DEFAULT")

        var fileMimeType = "application/msword"

        intent.setDataAndType(Uri.fromFile(file), fileMimeType)

        try {
            context.startActivity(intent)

            return ""

        } catch (e: ActivityNotFoundException) {

            return "未找到可查看软件"

        }

    }

    fun gettime(date: Date): String? {

        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }


    fun writeResponseBodyToDisk2(body: ResponseBody, filename: String): Pair<Boolean, File> {
        try {
            // todo change the file location/name according to your needs
            //File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + filename);

            val futureStudioIconFile = File(Environment.getExternalStorageDirectory().toString() + File.separator
                    + "datrixdownload")
            if (!futureStudioIconFile.exists())
                futureStudioIconFile.mkdirs()
            val file = File(futureStudioIconFile, filename)
            ("filepath : " + file.absolutePath).LogD()
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream!!.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    // Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream!!.flush()

                return Pair(true, file)
            } catch (e: IOException) {
                e.toString().LogD("error1 : ")

                return Pair(false, file)
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            e.toString().LogD("error2 : ")
            return Pair(false, File("null"))
        }

    }
    fun writeResponseBodyToTempDisk2(body: ResponseBody, filename: String): Pair<Boolean, File> {
        try {
            // todo change the file location/name according to your needs
            //File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + filename);

            val futureStudioIconFile = File(Environment.getExternalStorageDirectory().toString() + File.separator
                    + "datrixdownloadTemp")
            if (!futureStudioIconFile.exists())
                futureStudioIconFile.mkdirs()
            val file = File(futureStudioIconFile, filename)
            ("filepath : " + file.absolutePath).LogD()
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream!!.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    // Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream!!.flush()

                return Pair(true, file)
            } catch (e: IOException) {
                e.toString().LogD("error1 : ")

                return Pair(false, file)
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            e.toString().LogD("error2 : ")
            return Pair(false, File("null"))
        }

    }

    fun writeResponseBodyToDisk(body: ResponseBody, filename: String): Boolean {
        try {
            // todo change the file location/name according to your needs
            //File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + filename);

            val futureStudioIconFile = File(Environment.getExternalStorageDirectory().toString() + File.separator
                    + "datrixdownload")
            if (!futureStudioIconFile.exists())
                futureStudioIconFile.mkdirs()
            val file = File(futureStudioIconFile, filename)
            ("filepath : " + file.absolutePath).LogD("file path : ")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream!!.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    // Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream!!.flush()

                return true
            } catch (e: IOException) {
                e.toString().LogD("error1 : ")

                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            e.toString().LogD("error2 : ")
            return false
        }

    }

    fun getTXTFileString(inputStream: InputStream): String {
        var inputStreamReader: InputStreamReader? = null
        try {
            inputStreamReader = InputStreamReader(inputStream, "utf-8")
        } catch (e1: UnsupportedEncodingException) {
            e1.printStackTrace()
        }

        val reader = BufferedReader(inputStreamReader)
        val sb = StringBuilder("")
        var line: String
        try {

//            while (true) {
//                val byteCount = ins.read(buf)
//                if (byteCount < 0) break
//                out.write(buf, 0, byteCount)
//
//            }


            while (true) {
                val line = reader.readLine() ?: break
                sb.append(line)
                sb.append("\n")

            }

//            while ((line = reader.readLine()) != null) {
//                sb.append(line)
//                sb.append("\n")
//            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return sb.toString()
    }

    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    val MY_PERMISSIONS_CAMER = 1233

    fun checkPermissionREAD_EXTERNAL_STORAGE(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {


            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE)

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context as Activity,
                                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                return false
            } else {
                return true
            }


        } else {
            return true
        }
    }

    fun checkPermissionCAMERA(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {


            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA)) {
                    showDialog("External storage", context,
                            Manifest.permission.CAMERA)

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context as Activity,
                                    arrayOf<String>(Manifest.permission.CAMERA),
                                    MY_PERMISSIONS_CAMER)
                }
                return false
            } else {
                return true
            }


        } else {
            return true
        }
    }


    fun showDialog(msg: String, context: Context,
                   permission: String) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage(msg + " permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes
        ) { _, _ ->
            ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        val alert = alertBuilder.create()
        alert.show()
    }


    fun delFolder(folderPath: String) {
        try {
            delAllFile(folderPath) //删除完里面所有内容
            var filePath = folderPath
            filePath = filePath.toString()
            val myFilePath = java.io.File(filePath)
            myFilePath.delete() //删除空文件夹
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * metrics.density
    }

    fun delAllFile(path: String): Boolean {
        var flag = false
        val file = File(path)
        if (!file.exists()) {
            return flag
        }
        if (!file.isDirectory) {
            return flag
        }
        val tempList = file.list()
        var temp: File? = null
        for (i in tempList.indices) {
            if (path.endsWith(File.separator)) {
                temp = File(path + tempList[i])
            } else {
                temp = File(path + File.separator + tempList[i])
            }
            if (temp.isFile) {
                temp.delete()
            }
            if (temp.isDirectory) {
                delAllFile(path + "/" + tempList[i])//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i])//再删除空文件夹
                flag = true
            }
        }
        return flag
    }


    fun secToTime(time: Int): String {
        var timeStr: String? = null
        var hour = 0
        var minute = 0
        var second = 0
        if (time <= 0)
            return "00:00"
        else {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99)
                    return "99:59:59"
                minute = minute % 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    fun unitFormat(i: Int): String {
        var retStr: String? = null
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i)
        else
            retStr = "" + i
        return retStr
    }

}

