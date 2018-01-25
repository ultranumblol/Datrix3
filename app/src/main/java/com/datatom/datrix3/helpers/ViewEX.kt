package com.datatom.datrix3.helpers

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by wgz on 2017/11/2.
 */
fun View.Show() {
    visibility = View.VISIBLE

}

fun View.hide() {

    visibility = View.GONE


}

fun View.inVisible() {

    visibility = View.INVISIBLE


}


fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun Snackbar.showLong(string: String,root :View){
    Snackbar.make(root,string,Snackbar.LENGTH_LONG).show()

}