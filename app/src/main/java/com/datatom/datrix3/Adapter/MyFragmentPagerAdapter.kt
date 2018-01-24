package com.datatom.datrix3.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

/**
 * Created by wgz on 2018/1/23.
 */
class MyFragmentPagerAdapter(fm: FragmentManager, list: ArrayList<Fragment>, internal var titles: List<String>) : FragmentPagerAdapter(fm) {
    internal var list: List<Fragment>

    init {
        this.list = list
    }

    /*
    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){
            LogUtil.d("error");
            //Log.d("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }*/

    override fun getItem(position: Int): Fragment {
        return list[position]


    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}
