package com.datatom.datrix3.listener

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import io.reactivex.disposables.Disposable


/**
 * Created by wgz on 2018/3/9.
 */
interface RxActionManager {

    fun add(tag: String, disposable: Disposable)

    fun remove(tag: String)

    fun removeAll()

    fun cancel(tag: String)

    fun cancelAll()
}