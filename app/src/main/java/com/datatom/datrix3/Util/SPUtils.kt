package com.datatom.datrix3.Util

import android.content.Context
import android.content.SharedPreferences

class SPUtils {
    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * 保存在手机里面的文件名 和读写模式
         */
        val FILE_NAME = "share_data"
        val MODE = Context.MODE_PRIVATE


        /**
         * 异步提交方法
         * @param context
         * @param key
         * @param object
         */
        fun putApply(context: Context, key: String, `object`: Any) {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)
            val editor = sp.edit()
            judgePutDataType(key, `object`, editor)
            editor.apply()
        }

        /**
         * 同步提交方法
         * @param context
         * @param key
         * @param object
         * @return
         */
        fun putCommit(context: Context, key: String, `object`: Any): Boolean {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)
            val editor = sp.edit()
            judgePutDataType(key, `object`, editor)
            return editor.commit()
        }

        /**
         * 根据不同类型 使用不同的写入方法
         * @param key
         * @param object
         * @param editor
         */
        private fun judgePutDataType(key: String, `object`: Any, editor: SharedPreferences.Editor) {
            if (`object` is String) {
                editor.putString(key, `object`)
            } else if (`object` is Int) {
                editor.putInt(key, `object`)
            } else if (`object` is Boolean) {
                editor.putBoolean(key, `object`)
            } else if (`object` is Float) {
                editor.putFloat(key, `object`)
            } else if (`object` is Long) {
                editor.putLong(key, `object`)
            } else if (`object` is Set<*>) {
                editor.putStringSet(key, `object` as Set<String>)
            } else {
                editor.putString(key, `object`.toString())
            }
        }

        fun putAdd(editor: SharedPreferences.Editor, key: String, `object`: Any) {
            judgePutDataType(key, `object`, editor)
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         *
         * @param context
         * @param key
         * @param defaultObject
         * @return
         */
        operator fun get(context: Context, key: String, defaultObject: Any): Any? {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)

            if (defaultObject is String) {
                return sp.getString(key, defaultObject)
            } else if (defaultObject is Int) {
                return sp.getInt(key, defaultObject)
            } else if (defaultObject is Boolean) {
                return sp.getBoolean(key, defaultObject)
            } else if (defaultObject is Float) {
                return sp.getFloat(key, defaultObject)
            } else if (defaultObject is Long) {
                return sp.getLong(key, defaultObject)
            } else if (defaultObject is Set<*>) {
                return sp.getStringSet(key, defaultObject as Set<String>)
            }

            return null
        }

        operator fun get(context: Context, key: String, defaultObject: Any, filename: String): Any? {
            val sp = context.getSharedPreferences(filename,
                    MODE)

            if (defaultObject is String) {
                return sp.getString(key, defaultObject)
            } else if (defaultObject is Int) {
                return sp.getInt(key, defaultObject)
            } else if (defaultObject is Boolean) {
                return sp.getBoolean(key, defaultObject)
            } else if (defaultObject is Float) {
                return sp.getFloat(key, defaultObject)
            } else if (defaultObject is Long) {
                return sp.getLong(key, defaultObject)
            } else if (defaultObject is Set<*>) {
                return sp.getStringSet(key, defaultObject as Set<String>)
            }

            return null
        }


        /**
         * 移除某个key值已经对应的值
         *
         * @param context
         * @param key
         */
        fun remove(context: Context, key: String) {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)
            val editor = sp.edit()
            editor.remove(key)
            editor.apply()
        }

        /**
         * 清除所有数据
         *
         * @param context
         */
        fun clear(context: Context) {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)
            val editor = sp.edit()
            editor.clear()
            editor.apply()
        }

        /**
         * 查询某个key是否已经存在
         *
         * @param context
         * @param key
         * @return
         */
        fun contains(context: Context, key: String): Boolean {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)
            return sp.contains(key)
        }

        /**
         * 返回所有的键值对
         *
         * @param context
         * @return
         */
        fun getAll(context: Context): Map<String, *> {
            val sp = context.getSharedPreferences(FILE_NAME,
                    MODE)
            return sp.all
        }
    }

}