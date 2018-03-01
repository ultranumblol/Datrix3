package com.datatom.datrix3.view

import android.content.Context
import android.graphics.Matrix
import android.os.Handler
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast


import com.datatom.datrix3.Bean.SwitchVideoModel
import com.datatom.datrix3.R
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

import java.io.File
import java.util.ArrayList

/**
 * Created by shuyu on 2016/12/7.
 * 注意
 * 这个播放器的demo配置切换到全屏播放器
 * 这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
 */

class SampleVideo : StandardGSYVideoPlayer {

    private var mMoreScale: TextView? = null

    private var mSwitchSize: TextView? = null

    private var mChangeRotate: TextView? = null

    private var mChangeTransform: TextView? = null

    private var mUrlList: List<SwitchVideoModel> = ArrayList()

    //记住切换数据源类型
    private var mType = 0

    private var mTransformSize = 0

    //数据源
    private var mSourcePosition = 0

    private var mTypeText = "标准"

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag!!) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun init(context: Context) {
        super.init(context)
        initView()
    }

    private fun initView() {
        mMoreScale = findViewById(R.id.moreScale)
        mSwitchSize = findViewById(R.id.switchSize)
        mChangeRotate = findViewById(R.id.change_rotate)
        mChangeTransform = findViewById(R.id.change_transform)

        //切换清晰度
        mMoreScale!!.setOnClickListener(OnClickListener {
            if (!mHadPlay) {
                return@OnClickListener
            }
            if (mType == 0) {
                mType = 1
            } else if (mType == 1) {
                mType = 2
            } else if (mType == 2) {
                mType = 3
            } else if (mType == 3) {
                mType = 4
            } else if (mType == 4) {
                mType = 0
            }
            resolveTypeUI()
        })

        //        //切换视频清晰度
        //        mSwitchSize.setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                showSwitchDialog();
        //            }
        //        });

        //旋转播放角度
        mChangeRotate!!.setOnClickListener(OnClickListener {
            if (!mHadPlay) {
                return@OnClickListener
            }
            if (mTextureView.rotation - mRotate == 270f) {
                mTextureView.rotation = mRotate.toFloat()
                mTextureView.requestLayout()
            } else {
                mTextureView.rotation = mTextureView.rotation + 90
                mTextureView.requestLayout()
            }
        })

        //镜像旋转
        mChangeTransform!!.setOnClickListener(OnClickListener {
            if (!mHadPlay) {
                return@OnClickListener
            }
            if (mTransformSize == 0) {
                mTransformSize = 1
            } else if (mTransformSize == 1) {
                mTransformSize = 2
            } else if (mTransformSize == 2) {
                mTransformSize = 0
            }
            resolveTransform()
        })

    }

    /**
     * 需要在尺寸发生变化的时候重新处理
     */
    override fun onSurfaceSizeChanged(surface: Surface?, width: Int, height: Int) {
        super.onSurfaceSizeChanged(surface, width, height)
        resolveTransform()
    }

    override fun onSurfaceAvailable(surface: Surface) {
        super.onSurfaceAvailable(surface)
        resolveRotateUI()
        resolveTransform()
    }

    /**
     * 处理镜像旋转
     * 注意，暂停时
     */
    protected fun resolveTransform() {
        when (mTransformSize) {
            1 -> {
                val transform = Matrix()
                transform.setScale(-1f, 1f, (mTextureView.width / 2).toFloat(), 0f)
                mTextureView.setTransform(transform)
                mChangeTransform!!.text = "左右镜像"
                mTextureView.invalidate()
            }
            2 -> {
                val transform = Matrix()
                transform.setScale(1f, -1f, 0f, (mTextureView.height / 2).toFloat())
                mTextureView.setTransform(transform)
                mChangeTransform!!.text = "上下镜像"
                mTextureView.invalidate()
            }
            0 -> {
                val transform = Matrix()
                transform.setScale(1f, 1f, (mTextureView.width / 2).toFloat(), 0f)
                mTextureView.setTransform(transform)
                mChangeTransform!!.text = "旋转镜像"
                mTextureView.invalidate()
            }
        }
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */
    fun setUp(url: List<SwitchVideoModel>, cacheWithPlay: Boolean, title: String): Boolean {
        mUrlList = url
        return setUp(url[mSourcePosition].url, cacheWithPlay, title)
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     * @return
     */
    fun setUp(url: List<SwitchVideoModel>, cacheWithPlay: Boolean, cachePath: File, title: String): Boolean {
        mUrlList = url
        return setUp(url[mSourcePosition].url, cacheWithPlay, cachePath, title)
    }

    override fun getLayoutId(): Int {
        return R.layout.sample_video
    }


    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     *
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    override fun startWindowFullscreen(context: Context, actionBar: Boolean, statusBar: Boolean): GSYBaseVideoPlayer {
        val sampleVideo = super.startWindowFullscreen(context, actionBar, statusBar) as SampleVideo
        sampleVideo.mSourcePosition = mSourcePosition
        sampleVideo.mType = mType
        sampleVideo.mTransformSize = mTransformSize
        sampleVideo.mUrlList = mUrlList
        sampleVideo.mTypeText = mTypeText
        //sampleVideo.resolveTransform();
        sampleVideo.resolveTypeUI()
        //sampleVideo.resolveRotateUI();
        //这个播放器的demo配置切换到全屏播放器
        //这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
        //比如已旋转角度之类的等等
        //可参考super中的实现
        return sampleVideo
    }

    /**
     * 推出全屏时将对应处理参数逻辑返回给非播放器
     *
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer
     */
    override fun resolveNormalVideoShow(oldF: View?, vp: ViewGroup, gsyVideoPlayer: GSYVideoPlayer?) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)
        if (gsyVideoPlayer != null) {
            val sampleVideo = gsyVideoPlayer as SampleVideo?
            mSourcePosition = sampleVideo!!.mSourcePosition
            mType = sampleVideo.mType
            mTransformSize = sampleVideo.mTransformSize
            mTypeText = sampleVideo.mTypeText
            setUp(mUrlList, mCache, mCachePath, mTitle)
            resolveTypeUI()
        }
    }

    /**
     * 旋转逻辑
     */
    private fun resolveRotateUI() {
        if (!mHadPlay) {
            return
        }
        mTextureView.rotation = mRotate.toFloat()
        mTextureView.requestLayout()
    }

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private fun resolveTypeUI() {
        if (!mHadPlay) {
            return
        }
        if (mType == 1) {
            mMoreScale!!.text = "16:9"
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9)
        } else if (mType == 2) {
            mMoreScale!!.text = "4:3"
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3)
        } else if (mType == 3) {
            mMoreScale!!.text = "全屏"
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)
        } else if (mType == 4) {
            mMoreScale!!.text = "拉伸全屏"
            GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
        } else if (mType == 0) {
            mMoreScale!!.text = "默认比例"
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT)
        }
        changeTextureViewShowType()
        if (mTextureView != null)
            mTextureView.requestLayout()
        mSwitchSize!!.text = mTypeText
    }

    //    /**
    //     * 弹出切换清晰度
    //     */
    //    private void showSwitchDialog() {
    //        if (!mHadPlay) {
    //            return;
    //        }
    //        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext());
    //        switchVideoTypeDialog.initList(mUrlList, new SwitchVideoTypeDialog.OnListItemClickListener() {
    //            @Override
    //            public void onItemClick(int position) {
    //                final String name = mUrlList.get(position).getName();
    //                if (mSourcePosition != position) {
    //                    if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
    //                            || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)
    //                            && getGSYVideoManager().getMediaPlayer() != null) {
    //                        final String url = mUrlList.get(position).getUrl();
    //                        onVideoPause();
    //                        final long currentPosition = mCurrentPosition;
    //                        getGSYVideoManager().releaseMediaPlayer();
    //                        cancelProgressTimer();
    //                        hideAllWidget();
    //                        new Handler().postDelayed(new Runnable() {
    //                            @Override
    //                            public void run() {
    //                                setUp(url, mCache, mCachePath, mTitle);
    //                                setSeekOnStart(currentPosition);
    //                                startPlayLogic();
    //                                cancelProgressTimer();
    //                                hideAllWidget();
    //                            }
    //                        }, 500);
    //                        mTypeText = name;
    //                        mSwitchSize.setText(name);
    //                        mSourcePosition = position;
    //                    }
    //                } else {
    //                    Toast.makeText(getContext(), "已经是 " + name, Toast.LENGTH_LONG).show();
    //                }
    //            }
    //        });
    //        switchVideoTypeDialog.show();
    //    }


}
