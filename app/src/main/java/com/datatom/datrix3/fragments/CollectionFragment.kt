package com.datatom.datrix3.fragments

import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.FrameLayout
import com.datatom.datrix3.Adapter.mycollectadapter
import com.datatom.datrix3.base.BaseFragment
import com.datatom.datrix3.R
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.RxBus
import com.datatom.datrix3.helpers.Show
import com.datatom.datrix3.helpers.hide
import com.jude.easyrecyclerview.EasyRecyclerView
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class CollectionFragment : BaseFragment(),View.OnClickListener {


    var rv: EasyRecyclerView? = null

    var rvadapter: mycollectadapter? = null

    private var zhezhao : FrameLayout? = null

    private var cardview : CardView? = null

    override fun initview(view: View) {

        rv = view.find(R.id.rv_collect)

        zhezhao = view.find(I.collect_zhezhao)
        cardview = view.find(I.collection_chose_cardview)



        zhezhao!!.setOnClickListener(this)

        rvadapter = mycollectadapter(activity!!)

        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter

        }

       // var data = arrayListOf(1,2,3,4,5,6,7,8,9,0)


        //rvadapter!!.addAll(data)


        RxBus.get().toFlowable(String::class.java).subscribe{
            when(it){

                "collect_chose" -> {
                    cardview!!.visibility =
                            if (cardview!!.visibility == View.GONE) View.VISIBLE
                            else View.GONE

                    if (cardview!!.visibility == View.GONE) {
                        RxBus.get().post("hidezhezhao")
                        zhezhao!!.hide()
                    } else {
                        RxBus.get().post("showzhezhao")
                        zhezhao!!.Show()
                    }


                }
                "hidecardview" -> {
                    hideCradview()

                }

        }
        }


    }
    override fun onClick(v: View?) {

        when(v){

           zhezhao ->{

               hideCradview()

           }



        }


    }

    fun hideCradview(){

        RxBus.get().post("hidezhezhao")
        cardview!!.hide()
        zhezhao!!.hide()
    }





    override fun getLayoutitem(): Int {

        return R.layout.fragment_collection;
    }


}