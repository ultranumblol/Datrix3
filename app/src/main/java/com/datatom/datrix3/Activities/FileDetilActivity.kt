package com.datatom.datrix3.Activities


import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.SizeUtils
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.LogD
import kotlinx.android.synthetic.main.activity_file_detil.*

/**
 * 文件详情
 */
class FileDetilActivity : BaseActivity() {


    override fun ActivityLayout(): Int {

        return R.layout.activity_file_detil
    }

    override fun initView() {


        setToolbartitle("详情信息")
        initdata()


    }

    private fun initdata() {

        var data = intent.getSerializableExtra("file") as PersonalFilelistData.result2

        HttpUtil.instance.apiService().filebasicinfo(Someutil.getToken(),data.objid,data.createuid,data.userid)
                .compose(RxSchedulers.compose())
                .subscribe({
                   // it.toString().LogD("result : ")
                    tv_filepath.text = "个人目录"
                    tv_filecollectnum.text = it.reuslt.state_fav
                    tv_filecreaterid.text = it.reuslt.createuid
                    tv_filecreatetime.text = it.reuslt.createtime
                    tv_filedownnum.text = it.reuslt.state_dload
                    tv_filegps.text = it.reuslt.gpsdistrict
                    tv_filegroup.text = it.reuslt.groupid
                    tv_filegroupnickname.text = it.reuslt.groupnickname
                    tv_filemodifytime.text = it.reuslt.modiftime
                    tv_filename.text = it.reuslt.filename
                    tv_filesize.text = SizeUtils.getSize(it.reuslt.filesize)
                    tv_fileviewnum.text = it.reuslt.state_view

                },{
                    it.toString().LogD("error : ")
                })



    }
}
