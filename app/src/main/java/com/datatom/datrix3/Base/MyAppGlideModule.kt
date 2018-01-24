package com.datatom.datrix3.Base

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream


/**
 * Created by wgz on 2018/1/22.
 */


@GlideModule
class MyAppGlideModule : AppGlideModule(){

    companion object {
        // Max cache size of glide.
        @JvmField
        val MAX_CACHE_SIZE = 1024 * 1024 * 512 // 512M

        // The cache directory name.
        @JvmField
        val CACHE_FILE_NAME = "IMG_CACHE" // cache file dir name

    }

    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        super.applyOptions(context, builder)

        // 36MB, memory cache size
        // default value: 24MB
        val memoryCacheSize = 1024 * 1024 * 36
        builder?.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))

        // Internal cache
        builder?.setDiskCache(InternalCacheDiskCacheFactory(context, CACHE_FILE_NAME, MAX_CACHE_SIZE.toLong()))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry?.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }


    /**
     * Disable the parsing of manifest file.
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}