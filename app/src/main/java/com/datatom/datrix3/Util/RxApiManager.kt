package com.datatom.datrix3.Util

import android.util.ArrayMap
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.listener.RxActionManager
import io.reactivex.disposables.Disposable
import tv.danmaku.ijk.media.player.IjkMediaPlayer.DefaultMediaCodecSelector.sInstance
import org.reactivestreams.Subscription





/**
 * Created by wgz on 2018/3/9.
 */
class RxApiManager  : RxActionManager {

    private val maps: HashMap<String, Disposable> = hashMapOf()
    override fun removeAll() {
        if (!maps!!.isEmpty()) {
            maps.clear();
        }
    }

    fun getmap(): HashMap<String, Disposable> {

        return maps
    }

    companion object {

        private var sInstance: RxApiManager? = null


    }

    fun get(): RxApiManager {

        if (sInstance == null) {
            synchronized(RxApiManager::class.java) {
                if (sInstance == null) {
                    sInstance = RxApiManager()
                }
            }
        }
        return sInstance!!
    }

    override fun add(tag: String, disposable: Disposable) {

        "添加任务".LogD()
        maps!!.put(tag, disposable)

    }

    override fun remove(tag: String) {

        if (!maps!!.isEmpty()) {
            maps!!.remove(tag)
        }
    }

    override fun cancel(tag: String) {



        if (maps!![tag]!! != null){
            "取消任务".LogD()
            maps[tag]!!.dispose()
            maps.remove(tag)
        }



    }

    override fun cancelAll() {


    }


}