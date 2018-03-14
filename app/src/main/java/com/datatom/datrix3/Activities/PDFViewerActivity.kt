package com.datatom.datrix3.Activities

import android.graphics.Color
import com.datatom.datrix3.R
import com.datatom.datrix3.BaseActivity
import com.datatom.datrix3.Bean.PersonalFilelistData
import com.datatom.datrix3.Bean.TaskFile
import com.datatom.datrix3.Util.HttpUtil
import com.datatom.datrix3.Util.Someutil
import com.datatom.datrix3.helpers.LogD
import com.datatom.datrix3.helpers.Show
import com.datatom.datrix3.helpers.hide
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfDocument
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_pdfviewer.*
import org.jetbrains.anko.toast
import java.io.File


class PDFViewerActivity : BaseActivity(), OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {


    var pageNumber = 0

    lateinit var pdfFileName: String
    override fun ActivityLayout(): Int {

        return R.layout.activity_pdfviewer
    }

    override fun initView() {

        pdfView.setBackgroundColor(Color.WHITE)

        pdfFileName = "ceshi "
        pdf_progressbar.Show()
        loadData()


    }

    private fun loadData() {

        var data = intent.getSerializableExtra("file")

        when(data){
            is PersonalFilelistData.result2 ->{
                pdfFileName = data.filename
                setToolbartitle(pdfFileName)
                var url = HttpUtil.BASEAPI_URL + "/datrix3/viewer/dcomp.php?fileidstr=" + data.fileid + "&iswindows=0&optuser=admin"
                var downpdf = HttpUtil.instance.downLoadApi().downloadFileWithFixedUrl(url)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe({

                            var result = Someutil.writeResponseBodyToDisk2(it, data.filename)
                            var file = result.second

                            if (result.first) {
                                "下载成功".LogD()

                                runOnUiThread {
                                    pdf_progressbar.hide()
                                    pdfView.fromFile(file)
                                            .defaultPage(pageNumber)
                                            .onPageChange(this)
                                            .enableAnnotationRendering(true)
                                            .onLoad(this)
                                            .scrollHandle(DefaultScrollHandle(this))
                                            .spacing(10) // in dp
                                            .onPageError(this)
                                            .load()
                                }

                            } else {
                                "下载失败".LogD()

                            }
                        }, {
                            it.toString().LogD("download error : ")
                        })

                addsub(downpdf)

            }
            is TaskFile ->{
                pdfFileName = data.filename
                setToolbartitle(pdfFileName)
                runOnUiThread {
                    pdf_progressbar.hide()
                    pdfView.fromFile(File(data.filePath))
                            .defaultPage(pageNumber)
                            .onPageChange(this)
                            .enableAnnotationRendering(true)
                            .onLoad(this)
                            .scrollHandle(DefaultScrollHandle(this))
                            .spacing(10) // in dp
                            .onPageError(this)
                            .load()
                }

            }

        }








    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        setToolbartitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount))
    }

    override fun loadComplete(nbPages: Int) {
        val meta = pdfView.documentMeta
        meta.title.LogD("title : ")
        meta.author.LogD("author : ")
        meta.subject.LogD("subject : ")
        meta.keywords.LogD("keywords : ")
        meta.creator.LogD("creator : ")
        meta.producer.LogD("producer : ")
        meta.creationDate.LogD("creationDate : ")
        meta.modDate.LogD("modDate : ")

        printBookmarksTree(pdfView.tableOfContents, "-")

    }

    fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
        for (b in tree) {

            String.format("%s %s, p %d", sep, b.title, b.pageIdx).LogD()

            if (b.hasChildren()) {
                printBookmarksTree(b.children, sep + "-")
            }
        }
    }

    override fun onPageError(page: Int, t: Throwable?) {
        toast("不能加载页面" + page)

    }
}
