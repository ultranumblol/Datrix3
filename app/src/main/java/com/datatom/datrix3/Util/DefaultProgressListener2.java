package com.datatom.datrix3.Util;


import android.util.Log;

import com.datatom.datrix3.listener.ProgressListener;

/**
 * Created by wgz on 2016/11/9.
 */

public class DefaultProgressListener2 implements ProgressListener {



    //多文件上传时，index作为上传的位置的标志
    private int mIndex;
    private int filepieces;

    public DefaultProgressListener2(int mIndex, int filepieces) {
        this.mIndex = mIndex;
        this.filepieces = filepieces;
    }

    @Override
    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
        int percent = (int) (hasWrittenLen * 100 / totalLen);
        if (percent > 100) percent = 100;
        if (percent < 0) percent = 0;
        long progress = (hasWrittenLen * 100 / totalLen)/filepieces +(100/filepieces)*mIndex;

        Log.d("wgz","pro : "+progress);
        //RxBus.getDefault().post(new ChatUpProgress(progress+"%"));

    }
}
