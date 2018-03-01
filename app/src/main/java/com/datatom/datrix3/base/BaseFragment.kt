package com.datatom.datrix3.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.datatom.datrix3.Util.Someutil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by wgz on 2018/1/23.
 */
abstract class BaseFragment : Fragment(){

    private var msubscription: CompositeDisposable? = null//管理所有的订阅

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutitem(), null)
        // View view = inflater.inflate(getLayoutitem(),container,false);

        initview(view)
        msubscription = CompositeDisposable()
        return view
    }

    fun addsub(disposable: Disposable) {
        msubscription?.add(disposable)

    }

    abstract fun initview(view: View)
    abstract fun getLayoutitem(): Int

    override fun onDestroyView() {
        super.onDestroyView()
        if (this.msubscription != null && msubscription!!.isDisposed) {
            this.msubscription!!.dispose()
        }

    }

    override fun onResume() {
        super.onResume()
        if (System.currentTimeMillis() - Someutil.getlastLogintime() > AppConstant.TOKEN_LASTTIME){
            Someutil.updateToken()

        }

    }

}