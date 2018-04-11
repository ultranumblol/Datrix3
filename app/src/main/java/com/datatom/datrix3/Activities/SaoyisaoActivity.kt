package com.datatom.datrix3.Activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.SurfaceHolder
import android.view.View
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.helpers.hide
import com.datatom.datrix3.view.zxing.camera.CameraManager
import com.datatom.datrix3.view.zxing.decoding.CaptureActivityHandler
import com.datatom.datrix3.view.zxing.decoding.InactivityTimer
import com.datatom.datrix3.view.zxing.view.ViewfinderView
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_saoyisao.*
import java.io.IOException
import java.util.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN




class SaoyisaoActivity : BaseActivity(), SurfaceHolder.Callback {


    private val BEEP_VOLUME = 0.10f
    private val VIBRATE_DURATION = 200L
    private var handler: CaptureActivityHandler? = null
    private var hasSurface: Boolean = false
    private var decodeFormats: Vector<BarcodeFormat>? = null
    private var characterSet: String? = null
    private var inactivityTimer: InactivityTimer? = null
    private var mediaPlayer: MediaPlayer? = null
    private var playBeep: Boolean = false
    private var vibrate: Boolean = false
    private val beepListener = MediaPlayer.OnCompletionListener { mp -> mp.seekTo(0) }

    override fun ActivityLayout(): Int {

        return R.layout.activity_saoyisao
    }


    override fun onResume() {
        super.onResume()



        val surfaceHolder = preview_view.getHolder()
        if (hasSurface) {
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(this)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
        decodeFormats = null
        characterSet = null

        playBeep = true
        val audioService = getSystemService(AUDIO_SERVICE) as AudioManager
        if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false
        }
        initBeepSound()
        vibrate = true
    }

    override fun onPause() {
        super.onPause()
        if (handler != null) {
            handler!!.quitSynchronously()
            handler = null
        }
        CameraManager.get().closeDriver()
    }

    override fun onDestroy() {
        inactivityTimer!!.shutdown()
        super.onDestroy()
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder)
        } catch (ioe: IOException) {
            return
        } catch (e: RuntimeException) {
            return
        }

        if (handler == null) {
            handler = CaptureActivityHandler(this, decodeFormats,
                    characterSet)
        }
    }

    private fun initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setOnCompletionListener(beepListener)

            val file = resources.openRawResourceFd(
                    R.raw.beep)
            try {
                mediaPlayer!!.setDataSource(file.fileDescriptor,
                        file.startOffset, file.length)
                file.close()
                mediaPlayer!!.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                mediaPlayer = null
            }

        }
    }

    override fun initView() {
        CameraManager.init(application)
        hasSurface = false
        inactivityTimer = InactivityTimer(this)
        setToolbartitle("扫一扫")
        toolBar!!.hide()
        backimg.setOnClickListener {
            onBackPressed()
        }

        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {


    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        hasSurface = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder!!)
        }
    }

    fun getViewfinderView(): ViewfinderView {
        return viewfinder_view
    }

    fun drawViewfinder() {
        viewfinder_view.drawViewfinder()

    }

    fun handleDecode(result: Result, barcode: Bitmap) {
        inactivityTimer!!.onActivity()
        playBeepSoundAndVibrate()
        val resultString = result.text
        if (resultString == "") {
        } else {
            /*Intent resultIntent = new Intent();
            resultIntent.setClass(SaoYiSaoActivity.this, SecondCodeShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            //bundle.putParcelable("bitmap", barcode);
            resultIntent.putExtras(bundle);
            //RxBus.getDefault().post(new SecondCode(resultString,barcode));
            startActivityForResult(resultIntent,20);*/
            //ToastUtil.showLong(this,""+resultString);
            // startActivity(Intent(this@SaoYiSaoActivity, SecondCodeShowActivity::class.java).putExtra("result", resultString))
            // resultIntent.putExtras(bundle);
            //this.setResult(RESULT_OK, resultIntent);
            this@SaoyisaoActivity.finish()
        }

    }

    @SuppressLint("MissingPermission")
    private fun playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer!!.start()
        }
        if (vibrate) {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VIBRATE_DURATION)
        }
    }

    fun getHandler(): Handler {
        return handler!!
    }
}
