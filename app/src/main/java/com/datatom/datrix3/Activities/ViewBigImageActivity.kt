package com.datatom.datrix3.Activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.datatom.datrix3.Base.GlideApp
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.R
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.MD5

import java.util.concurrent.ExecutionException

import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.File


/**
 * 用于查看大图
 *
 * @author jingbin
 */
class ViewBigImageActivity : FragmentActivity(), OnPageChangeListener, PhotoViewAttacher.OnPhotoTapListener {

    // 接收传过来的uri地址
    internal var imageuri: List<String>? = null
    // 接收穿过来当前选择的图片的数量
    internal var code: Int = 0
    // 用于判断是头像还是文章图片 1:头像 2：文章大图
    internal var selet: Int = 0
    // 用于管理图片的滑动
    internal lateinit var very_image_viewpager: ViewPager
    /**
     * 显示当前图片的页数
     */
    internal lateinit var very_image_viewpager_text: TextView
    internal lateinit var adapter: ViewPagerAdapter


    // 当前页数
    private var page: Int = 0

    /**
     * 用于判断是否是加载本地图片
     */
    private var isLocal: Boolean = false
    /**
     * 本应用图片的id
     */
    private var imageId: Int = 0
    /**
     * 是否是本应用中的图片
     */
    private var isApp: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_big_image)

        getView()
    }

    /**
     * Glide 获得图片缓存路径
     */
    private fun getImagePath(imgUrl: String): String? {
        var path: String? = null
        val future = Glide.with(this@ViewBigImageActivity)
                .load(imgUrl)
                .downloadOnly(500, 500)
        try {
            val cacheFile = future.get()
            path = cacheFile.absolutePath
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return path
    }


    /*
     * 接收控件
     */
    private fun getView() {
        /************************* 接收控件  */
        very_image_viewpager_text = findViewById<View>(R.id.very_image_viewpager_text) as TextView

        very_image_viewpager = findViewById<View>(R.id.very_image_viewpager) as ViewPager


        /************************* 接收传值  */
        val bundle = intent.extras
        code = bundle!!.getInt("code")
        selet = bundle.getInt("selet")
        isLocal = bundle.getBoolean("isLocal", false)
        imageuri = bundle.getStringArrayList("imageuri")
        /**是否是本应用中的图片 */
        isApp = bundle.getBoolean("isApp", false)
        /**本应用图片的id */
        imageId = bundle.getInt("id", 0)





        /**
         * 给viewpager设置适配器
         */
        if (isApp) {
            val myPageAdapter = MyPageAdapter()
            very_image_viewpager.adapter = myPageAdapter
            very_image_viewpager.isEnabled = false
        } else {
            adapter = ViewPagerAdapter()
            very_image_viewpager.adapter = adapter
            very_image_viewpager.currentItem = code
            page = code
            very_image_viewpager.setOnPageChangeListener(this)
            very_image_viewpager.isEnabled = false
            // 设定当前的页数和总页数
            if (selet == 2) {
                very_image_viewpager_text.text = (code + 1).toString() + " / " + imageuri!!.size
            }
        }
    }

    /**
     * 下面是对Viewpager的监听
     */
    override fun onPageScrollStateChanged(arg0: Int) {}

    override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

    /**
     * 本方法主要监听viewpager滑动的时候的操作
     */
    override fun onPageSelected(arg0: Int) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        very_image_viewpager_text.text = (arg0 + 1).toString() + " / " + imageuri!!.size
        page = arg0
    }

    override fun onPhotoTap(view: View, x: Float, y: Float) {
        finish()
    }

    override fun onOutsidePhotoTap() {
        //        finish();
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    /**
     * 本应用图片适配器
     */

    internal inner class MyPageAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return 1
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = layoutInflater.inflate(R.layout.viewpager_very_image, container, false)
            val zoom_image_view = view.findViewById<View>(R.id.zoom_image_view) as PhotoView
            val spinner = view.findViewById<View>(R.id.loading) as ProgressBar
            spinner.visibility = View.GONE
            if (imageId != 0) {
                zoom_image_view.setImageResource(imageId)
            }
            zoom_image_view.setOnPhotoTapListener(this@ViewBigImageActivity)
            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)

        }
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    internal inner class ViewPagerAdapter : PagerAdapter() {

        var inflater: LayoutInflater

        init {
            inflater = layoutInflater
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = inflater.inflate(R.layout.viewpager_very_image, container, false)
            val zoom_image_view = view.findViewById<PhotoView>(R.id.zoom_image_view)
            val spinner = view.findViewById<ProgressBar>(R.id.loading)
            // 保存网络图片的路径
            val adapter_image_Entity = getItem(position) as String
            //TODO
            val imageUrl: String
            if (isLocal) {
                imageUrl = "file://" + adapter_image_Entity

            } else {
                imageUrl = adapter_image_Entity
            }
            //ACache aCache = ACache.get(ViewBigImageActivity.this);
            //byte[] stream = aCache.getAsBinary(imageUrl);


            spinner.visibility = View.VISIBLE
            spinner.isClickable = false
            var  data = intent.getSerializableExtra("file")

            when (data) {
                is PersonalFilelistData.result2 -> {
                    HttpUtil.instance.apiService().getVerifyCode(Someutil.getToken(), data.fileid)
                            .compose(RxSchedulers.compose())
                            .subscribe({
                                var code = it.reuslt


                                var strs = it.reuslt.split(",")


                                var index = strs[1].toInt() + strs[3].toInt()
                                var length = strs[2].toInt()


                                var key = it.reuslt.substring(index, (index + length)).MD5()
                                var url2 = HttpUtil.BASEAPI_URL + "datrix3/viewer/read.php?type=preview&fileid=" + data.fileid +
                                        "&objectid=" + data.objid + "&createuid=" + data.createuid + "&code=" + code + "&key=" + key + "&token=" +
                                        Someutil.getToken() + "&quality=a"

                                url2.LogD("url 2 :: ")

                                GlideApp.with(this@ViewBigImageActivity).load(url2)
                                        .transition(DrawableTransitionOptions().crossFade(700))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .listener(object : RequestListener<Drawable> {
                                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                                e.toString().LogD(" loadfailed  error : ")
                                                return false

                                            }

                                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                                spinner.visibility = View.GONE

                                                val height = zoom_image_view.height

                                                val wHeight = windowManager.defaultDisplay.height
                                                if (height > wHeight) {
                                                    zoom_image_view.scaleType = ImageView.ScaleType.CENTER_CROP
                                                } else {
                                                    zoom_image_view.scaleType = ImageView.ScaleType.FIT_CENTER
                                                }
                                                return false

                                            }
                                        }
                                        )
                                        .into(zoom_image_view)

                            }, {})
                }
                is TaskFile -> {

                   // "taskfile imagefilepath :${data.filePath}".LogD()
                    GlideApp.with(this@ViewBigImageActivity).load(File(data.filePath))
                            .transition(DrawableTransitionOptions().crossFade(700))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                    e.toString().LogD(" loadfailed  error : ")
                                    return false

                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    spinner.visibility = View.GONE

                                    val height = zoom_image_view.height

                                    val wHeight = windowManager.defaultDisplay.height
                                    if (height > wHeight) {
                                        zoom_image_view.scaleType = ImageView.ScaleType.CENTER_CROP
                                    } else {
                                        zoom_image_view.scaleType = ImageView.ScaleType.FIT_CENTER
                                    }
                                    return false

                                }
                            }
                            )
                            .into(zoom_image_view)
                }

            }
            //var url = HttpUtil.BASE_URL +"preview/getImage?fileid=" + imageUrl + "&token="+Someutil.getToken()



//            var url =HttpUtil.BASEAPI_URL +"datrix3/viewer/read.php?type=thumb&fileid=" +data.fileid + "&objectid="+data.objid + "&createuid=" + data.
//                    createuid + "&name="+data.name + "&size="+data.size+"&ext="+data.ext + "&mimetype=undefined&rand=0.629739389064067"
//
//            url.LogD("url :::  ")


            zoom_image_view.setOnPhotoTapListener(this@ViewBigImageActivity)
            container.addView(view, 0)
            return view
        }

        override fun getCount(): Int {
            return if (imageuri == null || imageuri!!.size == 0) {
                0
            } else imageuri!!.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

        fun getItem(position: Int): Any {
            return imageuri!![position]
        }
    }
}
