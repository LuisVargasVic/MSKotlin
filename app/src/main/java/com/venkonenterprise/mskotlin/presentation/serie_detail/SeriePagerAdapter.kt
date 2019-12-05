package com.venkonenterprise.mskotlin.presentation.serie_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.venkonenterprise.mskotlin.domain.series.Serie
import com.venkonenterprise.mskotlin.presentation.series.SeriesFragment.Companion.SERIE_KEY

/**
 * Created by Luis Vargas on 2019-07-28.
 */

class SeriePagerAdapter(fm: FragmentManager, val serie: Serie) : FragmentPagerAdapter(fm) {
    private val mFragments: MutableList<Fragment> = ArrayList()
    private val mFragmentTitles: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        val fragment = mFragments[position]
        val bundle = Bundle()
        bundle.putSerializable(SERIE_KEY, serie)
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragments.add(fragment)
        mFragmentTitles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]
    }
}