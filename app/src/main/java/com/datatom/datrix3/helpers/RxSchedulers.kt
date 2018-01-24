import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wgz
 */

object RxSchedulers {
    fun <T> compose(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        /* if (!Utils.isNetworkConnected()) {
                                    Toast.makeText(context, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
                                }*/
                        //判断网络连接
                    }
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }


}
