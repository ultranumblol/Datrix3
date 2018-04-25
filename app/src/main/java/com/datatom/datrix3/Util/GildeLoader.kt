package com.datatom.datrix3.Util

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import android.widget.ImageView

import com.bumptech.glide.Priority
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.datatom.datrix3.Base.GlideApp


import com.datatom.datrix3.R

/**
 * Created by wgz on 2018/1/22.
 */
object GildeLoader{

    fun loadAvatar(imageView: ImageView, url: String?) {
        GlideApp.with(imageView.context)
                .asBitmap()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .load(url)
                .circleCrop()
                .error(R.drawable.ic_avatar_placeholder)
                .into(imageView)
    }

    fun loadNormal(imageView: ImageView, url: String) {
        if (url.endsWith(".gif")) {
            GlideApp.with(imageView.context)
                    .asGif()
                    .load(url)
                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
                    .error(R.drawable.bg_shot)
                    .into(imageView)
        } else {
            GlideApp.with(imageView.context)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
                    .error(R.drawable.bg_shot)
                    .into(imageView)
        }
    }

    fun loadNormal(imageView: ImageView, url: String,holder :Int) {
        if (url.endsWith(".gif")) {
            GlideApp.with(imageView.context)
                    .asGif()
                    .load(url)
                    .placeholder(holder)
                    .centerCrop()

                    .into(imageView)
        } else {
            GlideApp.with(imageView.context)
                    .asBitmap()
                    .load(url)
                    .placeholder(holder)
                    .centerCrop()

                    .into(imageView)
        }
    }


    fun loadHighQualityWithPalette(imageView: ImageView, url: String, callback: OnPaletteProcessingCallback) {
        if (url.endsWith(".gif")) {
            GlideApp.with(imageView.context)
                    .asGif()
                    .load(url)
                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
                    .error(R.drawable.bg_shot)
                    .priority(Priority.HIGH)
                    .into(imageView)
        } else {
            GlideApp.with(imageView.context)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.bg_shot)
                    .thumbnail(0.5f)
                    .centerCrop()
                    .error(R.drawable.bg_shot)
                    .priority(Priority.HIGH)
                    .into(object : BitmapImageViewTarget(imageView) {

                        // The function [onResourceReady] will called twice during one loading process.
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            super.onResourceReady(resource, transition)
                            resource?.let {
                                // The maximum color count is higher, the time of palette processing is more.
                                Palette.from(it).maximumColorCount(4).generate { palette ->
                                    callback.onPaletteGenerated(palette)
                                }
                            } ?: run {
                                callback.onPaletteNotAvailable()
                            }
                        }
                    })
        }
    }


}