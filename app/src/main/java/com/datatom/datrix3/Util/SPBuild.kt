package com.datatom.datrix3.Util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by wgz
 */
class SPBuild {
    private val editor: SharedPreferences.Editor

    constructor(context: Context) {
        this.editor = context.getSharedPreferences(SPUtils.FILE_NAME, SPUtils.MODE).edit()
    }

    constructor(context: Context, filename: String) {
        this.editor = context.getSharedPreferences(filename, SPUtils.MODE).edit()
    }

    fun addData(key: String, `object`: Any): SPBuild {
        SPUtils.putAdd(editor, key, `object`)
        return this
    }

    fun build() {
        this.editor.apply()
    }
}
