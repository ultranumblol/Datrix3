package com.datatom.datrix3.Activities

import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewCompat
import android.transition.Transition
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.MD5
import com.datatom.datrix3.helpers.hide
import com.datatom.datrix3.listener.OnTransitionListener
import com.datatom.datrix3.view.SampleVideo
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_play_video.*


class PlayVideoActivity : BaseActivity() {

    companion object {

        val IMG_TRANSITION = "IMG_TRANSITION"
        val TRANSITION = "TRANSITION"
    }


    private var isTransition: Boolean = true

    private var transition: Transition? = null


    internal var orientationUtils: OrientationUtils? = null

    override fun ActivityLayout(): Int {

        return R.layout.activity_play_video
    }


    override fun initView() {


        tool_bar.hide()
        isTransition = intent.getBooleanExtra(TRANSITION, false)
        setTranslucent(this)

        "onresume ------>".LogD()
        var data = intent.getSerializableExtra("file") as PersonalFilelistData.result2


        HttpUtil.instance.apiService().getVerifyCode(Someutil.getToken(), data.fileid)
                .compose(RxSchedulers.compose())
                .subscribe({

                    var code =  it.reuslt


                    var strs = it.reuslt.split(",")


                    var index = strs[1].toInt() + strs[3].toInt()
                    var length = strs[2].toInt()



                    var key = it.reuslt.substring(index , (index + length)).MD5()



                    var url2 = HttpUtil.BASEAPI_URL + "datrix3/viewer/read.php?type=origin&fileid=" + data.fileid +
                            "&objectid=" + data.objid + "&createuid=" + data.createuid +
                            "&code="+code+"&key=" +key +
                            "&token=" + Someutil.getToken() + "&quality=a"

//                    var url3 =   "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4"
//
//
//                    var url4 = "http://192.168.50.230/viewer/read.php?type=preview&fileid=2b20bad96983cf2bef7255d80ea632d4.wmv&objectid=20180227/16/2b20bad96983cf2bef7255d80ea632d4.wmv&createuid=test&code=4c429dd6d92cdfe19feb2ba9f3c12edc,3,1,3&key=8277e0910d750195b448797616e091ad&token="+Someutil.getToken()
//                    url2.LogD(" url :  ")
//


                    video_player.apply {
                        setUp(url2, false, data.filename)
                        backButton.visibility = View.VISIBLE
                        backButton.setOnClickListener { onBackPressed() }


                        setIsTouchWiget(true)

                        fullscreenButton.setOnClickListener {
                            orientationUtils!!.resolveByClick()
                        }


                    }

                    orientationUtils = OrientationUtils(this, video_player)

                    initTransition()


                }, {

                    it.toString().LogD("error : ")

                })




    }




    private fun initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(video_player, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            video_player.startPlayLogic()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener(): Boolean {
        transition = window.sharedElementEnterTransition
        if (transition != null) {
            transition!!.addListener(object : OnTransitionListener() {

                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    video_player.startPlayLogic()
                    transition.removeListener(this)
                }
//                fun onTransitionEnd(transition: Transition) {
//                    super.onTransitionEnd(transition)
//
//                }
            })
            return true
        }
        return false
    }

    fun setTranslucent(activity: Activity) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 设置根布局的参数
//            val rootView = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
//            rootView.fitsSystemWindows = true
//            rootView.clipToPadding = true
        }
    }

    override fun onPause() {
        super.onPause()
        video_player.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        video_player.onVideoResume()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onDestroy() {
        super.onDestroy()

        if (orientationUtils != null)
            orientationUtils!!.releaseListener()
    }

    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils!!.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            video_player.getFullscreenButton().performClick()
            return
        }
        //释放所有
        video_player.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
        finish()
//        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            finish()
//        } else {
//            Handler().postDelayed({
//                finish()
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
//            }, 200)
//        }
    }


}
