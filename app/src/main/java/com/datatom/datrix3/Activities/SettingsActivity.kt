package com.datatom.datrix3.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : BaseActivity() {


    override fun ActivityLayout(): Int {

        return R.layout.activity_settings
    }

    override fun initView() {
        setToolbartitle("设置")
        setdownfile_path.setOnClickListener {
            this.startActivity(android.content.Intent(this,ChoseDownloadDirActivity::class.java))
        }

    }

}
