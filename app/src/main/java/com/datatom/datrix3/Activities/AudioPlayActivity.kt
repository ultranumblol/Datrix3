package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.Util.Someutil.secToTime
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.Show
import com.datatom.datrix3.helpers.hide
import com.piterwilson.audio.MP3RadioStreamDelegate
import com.piterwilson.audio.MP3RadioStreamPlayer
import com.shuyu.gsyvideoplayer.utils.CommonUtil.dip2px
import com.shuyu.gsyvideoplayer.utils.CommonUtil.getScreenWidth
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_audio_play.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.util.*


class AudioPlayActivity : BaseActivity(), MP3RadioStreamDelegate, View.OnClickListener {


    internal var player: MP3RadioStreamPlayer? = null

    internal var timer: Timer? = null

    internal var playeEnd: Boolean = false

    internal var seekBarTouch: Boolean = false

    private var purl = ""

    override fun ActivityLayout(): Int {

        return R.layout.activity_audio_play
    }

    override fun initView() {
        var data = intent.getSerializableExtra("file")

        audioplay.hide()
        pro_loading.Show()
        //cardview_play.hide()
        //audioWave.hide()
        sb_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                seekBarTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                seekBarTouch = false
                if (!playeEnd) {
                    player!!.seekTo(seekBar.progress.toLong())
                }
            }
        })


        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                if (playeEnd || player == null || !sb_music.isEnabled) {
                    return
                }
                val position = player!!.curPosition
                if (position > 0 && !seekBarTouch) {
                    runOnUiThread {
                        if (!player!!.isPause)
                        audioplay.setImageResource(R.mipmap.music_button_pause)
                        tv_curr_play_time.text = secToTime((position/1000000).toInt())
                    }

                    sb_music.progress = position.toInt()

                }
            }
        }, 1000, 1000)


        when (data) {
            is PersonalFilelistData.result2 -> {
                setToolbartitle(data.filename)
                tv_filename.text = data.filename

                val tempFile = File(Environment.getExternalStorageDirectory().toString() + File.separator
                        + "datrixdownloadTemp/" +data.filename)
                if (tempFile.exists()){
                    purl = tempFile.absolutePath
                    runOnUiThread {
                        play()
                        cardview_play.Show()
                        audioplay.Show()
                        pro_loading.hide()
                    }

                }else{
                    var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + data.fileid + "&iswindows=0&optuser=admin"
                    url.LogD("url  : ")
                    val download = HttpUtil.instance.downLoadApi().downloadFileWithFixedUrl(url)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe({

                                "下载成功".LogD()
                               // Looper.prepare()
                                var result = Someutil.writeResponseBodyToTempDisk2(it, data.filename)
                                var file = result.second
                                if (result.first) {
                                    purl = file.absolutePath
//
                                    runOnUiThread {
                                        play()
                                        cardview_play.Show()
                                        audioplay.Show()
                                        pro_loading.hide()
                                    }

                                } else {
                                    this@AudioPlayActivity.toast("下载失败")
                                    runOnUiThread {
                                        pro_loading.hide()
                                    }

                                }
                            }, {
                                it.toString().LogD("error : ")

                                runOnUiThread {
                                    this@AudioPlayActivity.toast("下载失败")
                                        pro_loading.hide()
                                }

                            })
                    addsub(download)
                }
            }
        }

        audioplay.setOnClickListener(this)


    }


    override fun onDestroy() {
        super.onDestroy()
        audioWave.stopView()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        stop()
    }

    private fun play() {
        if (player != null) {
            player!!.stop()
            player!!.release()
            player = null
        }
        player = MP3RadioStreamPlayer()
        //player.setUrlString(this, true, "http://www.stephaniequinn.com/Music/Commercial%20DEMO%20-%2005.mp3");
        player!!.urlString = purl
        player!!.delegate = this

        val size = getScreenWidth(this) / dip2px(this, 1f)//控件默认的间隔是1
        player!!.setDataList(audioWave.recList, size)
        //player.setWaveSpeed(1100);
        //mRecorder.setDataList(audioWave.getRecList(), size);
        //player.setStartWaveTime(5000);
        //audioWave.setDrawBase(false);
        // audioWave.setBaseRecorder(player)
        // audioWave.startView()

        try {
            audioplay.setImageResource(R.mipmap.music_button_pause)
            player!!.play()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun stop() {
        if (player != null)
            player!!.stop()

    }

    override fun onClick(v: View?) {

        when (v) {
            audioplay -> {


                if (playeEnd) {
                    stop()
                    audioplay.setImageResource(R.mipmap.music_button_pause)
                    sb_music.isEnabled = true
                    play()
                    return
                }

                if (player!!.isPause) {
                    audioplay.setImageResource(R.mipmap.music_button_pause)
                    player!!.isPause = false
                    sb_music.isEnabled = false
                } else {
                    audioplay.setImageResource(R.mipmap.music_button_play)
                    player!!.isPause = true
                    sb_music.isEnabled = true
                }


            }

        }
    }

    override fun onRadioPlayerPlaybackStarted(player: MP3RadioStreamPlayer?) {
        this.runOnUiThread {
            playeEnd = false
            audioplay.isEnabled = true
            sb_music.max = player!!.duration.toInt()
            tv_total_play_time.text = secToTime((player!!.duration/1000000).toInt())
            sb_music.isEnabled = true
        }
    }

    override fun onRadioPlayerStopped(player: MP3RadioStreamPlayer?) {
        this.runOnUiThread {
            playeEnd = true
            audioplay.setImageResource(R.mipmap.music_button_play)
            audioplay.isEnabled = true
            sb_music.isEnabled = false
        }
    }

    override fun onRadioPlayerError(player: MP3RadioStreamPlayer?) {
        this.runOnUiThread {
            playeEnd = false
            audioplay.isEnabled = true
            sb_music.isEnabled = false
        }
    }

    override fun onRadioPlayerBuffering(player: MP3RadioStreamPlayer?) {
        this.runOnUiThread {
            audioplay.isEnabled = false
            sb_music.isEnabled = false
        }
    }
}
