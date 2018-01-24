package com.datatom.datrix3.Util

import android.support.v7.graphics.Palette

/**
 * Created by wgz on 2018/1/22.
 */
interface OnPaletteProcessingCallback {

    /**
     * The [Palette] finishes its work successfully.
     */
    fun onPaletteGenerated(palette: Palette?)

    /**
     * The [Palette] finished its work with a failure.
     */
    fun onPaletteNotAvailable()

}