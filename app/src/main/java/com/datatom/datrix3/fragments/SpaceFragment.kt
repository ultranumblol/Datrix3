package com.datatom.datrix3.fragments

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.datatom.datrix3.Adapter.SpaceAdapter
import com.datatom.datrix3.Base.BaseFragment
import com.datatom.datrix3.Bean.SpaceType
import com.datatom.datrix3.R
import com.datatom.datrix3.helpers.I
import com.datatom.datrix3.helpers.RxBus
import com.datatom.datrix3.helpers.Show
import com.datatom.datrix3.helpers.hide
import com.jude.easyrecyclerview.EasyRecyclerView
import io.github.tonnyl.whatsnew.item.item
import io.github.tonnyl.whatsnew.item.whatsNew
import org.jetbrains.anko.find

/**
 * Created by wgz on 2018/1/23.
 */

class SpaceFragment : BaseFragment(), View.OnClickListener {


    private var rv: EasyRecyclerView? = null

    private var rvadapter: SpaceAdapter? = null

    private var editll: LinearLayout? = null

    private var editllmore: LinearLayout? = null

    private var rename: RelativeLayout? = null
    private var download: RelativeLayout? = null
    private var share: RelativeLayout? = null
    private var delete: RelativeLayout? = null
    private var more: RelativeLayout? = null
    private var dabaoxiazai: RelativeLayout? = null
    private var copy: RelativeLayout? = null
    private var move: RelativeLayout? = null
    private var detil: RelativeLayout? = null

    private var cardview: CardView? = null
    private var personspace: RelativeLayout? = null
    private var publicspace: RelativeLayout? = null

    private var teamspace: RelativeLayout? = null



    override fun initview(view: View) {

        rv = view.find(I.space_rv)

        editll = view.find(I.edit_ll)

        editllmore = view.find(I.edit_more_ll)

        rvadapter = SpaceAdapter(activity!!)

        rename = view.find(I.edit_rv_rename)
        download = view.find(I.edit_rv_xiazai)
        share = view.find(I.edit_rv_share)
        delete = view.find(I.edit_rv_delete)
        more = view.find(I.edit_rv_more)
        dabaoxiazai = view.find(I.edit_rv_dabaoxiazai)
        copy = view.find(I.edit_rv_copy)
        move = view.find(I.edit_rv_move)
        detil = view.find(I.edit_rv_detil)


        cardview = view.find(I.change_space_cardview)
        personspace = view.find(I.change_space_person)
        publicspace = view.find(I.change_space_public)
        teamspace = view.find(I.change_space_team)

        rename!!.setOnClickListener(this)
        download!!.setOnClickListener(this)
        share!!.setOnClickListener(this)
        delete!!.setOnClickListener(this)
        more!!.setOnClickListener(this)
        dabaoxiazai!!.setOnClickListener(this)
        copy!!.setOnClickListener(this)
        move!!.setOnClickListener(this)
        detil!!.setOnClickListener(this)

        personspace!!.setOnClickListener(this)
        publicspace!!.setOnClickListener(this)
        teamspace!!.setOnClickListener(this)



        cardview!!.hide()
        rv!!.apply {
            setLayoutManager(LinearLayoutManager(activity))
            adapter = rvadapter

        }

        var data = arrayOf(1, 2, 1, 23, 1, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 22)

        rvadapter!!.apply {

            addAll(data)
        }

        editll!!.hide()
        editllmore!!.hide()

        RxBus.get().toFlowable(String::class.java).subscribe {

            when (it) {
                "selectall" -> {

                    Log.d("wgz", "getcheckboxArrary :" + rvadapter!!.getcheckboxArrary().size())
                    Log.d("wgz", "adaptersize :" + rvadapter!!.count)

                    rvadapter!!.apply {
                        if (rvadapter!!.getcheckboxArrary().size() > 0 && rvadapter!!.getcheckboxArrary().size() == rvadapter!!.count) {
                            setCheckBoxNoneSelect()
                            notifyDataSetChanged()
                        } else {
                            setCheckBoxAllSelect()
                            notifyDataSetChanged()
                        }
                    }
                    RxBus.get().post("updatecheckbox")


                }
                "updatecheckbox" -> {
                    if (rvadapter!!.getcheckboxArrary().size() > 0) {
                        RxBus.get().post("hidemainbar")
                        editll!!.Show()

                    } else {
                        RxBus.get().post("showmainbar")
                        editll!!.hide()
                        editllmore!!.hide()
                    }

                }
                "changespace" -> {

                    cardview!!.visibility =
                            if (cardview!!.visibility == View.GONE) View.VISIBLE
                            else View.GONE

                    if (cardview!!.visibility == View.GONE){
                        RxBus.get().post("hidezhezhao")
                    }else{
                        RxBus.get().post("showzhezhao")
                    }

                }
                "hidecardview" ->{
                    cardview!!.hide()

                }

            }

        }


    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            I.edit_rv_rename -> {
            }
            I.edit_rv_xiazai -> {
            }
            I.edit_rv_share -> {
            }
            I.edit_rv_delete -> {
            }
            I.edit_rv_more -> {
                editllmore!!.visibility =
                        if (editllmore!!.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE

            }
            I.edit_rv_dabaoxiazai -> {
            }
            I.edit_rv_copy -> {
            }
            I.edit_rv_move -> {
            }
            I.edit_rv_detil -> {
            }
            I.change_space_person -> {
                RxBus.get().post("hidezhezhao")
                cardview!!.hide()
                RxBus.get().post(SpaceType("个人空间"))
            }
            I.change_space_public -> {
                RxBus.get().post("hidezhezhao")
                cardview!!.hide()
                RxBus.get().post(SpaceType("公共空间"))
            }
            I.change_space_team -> {
                RxBus.get().post("hidezhezhao")
                cardview!!.hide()
                RxBus.get().post(SpaceType("讨论组"))


            }

        }


    }

    override fun getLayoutitem(): Int {

        return R.layout.fragment_space;
    }


}