package com.datatom.datrix3.base


import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.app
import com.datatom.datrix3.database.AppDatabase
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.listener.ProgressListener

/**
 * Created by wgz on 2016/11/9.
 */

class DefaultProgressListener(private val file: TaskFile?) : ProgressListener {
    private var settime: Long = 0


    override fun onProgress(hasWrittenLen: Long, totalLen: Long, hasFinish: Boolean) {
        var percent = (hasWrittenLen * 100 / totalLen).toInt()
        if (percent > 100) percent = 100
        if (percent < 0) percent = 0
       // val progress = (hasWrittenLen + file!!.offset) * 100 / file.total

        if (file != null) {


            val progress = (hasWrittenLen + file.offset) * 100 / file.total
            val nowtime = System.currentTimeMillis()
            if (nowtime - settime > 300) {
                file.filepersent = progress.toInt()
                if (AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(file.id)!=null)
                file.forcestop = AppDatabase.getInstance(app.mapp).TaskFileDao().queryTaskFile(file.id).forcestop
                AppDatabase.getInstance(app.mapp).TaskFileDao().updatefiles(file)

                ("progress : " + progress).LogD()


                settime = nowtime

            }


        }

    }
}
