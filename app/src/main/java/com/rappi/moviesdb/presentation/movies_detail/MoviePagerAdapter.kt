package com.rappi.moviesdb.presentation.movies_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.presentation.movies.MoviesFragment.Companion.MOVIE_KEY

/**
 * Created by Luis Vargas on 2019-07-28.
 */

class MoviePagerAdapter(fm: FragmentManager, val movie: Movie) : FragmentPagerAdapter(fm) {
    private val mFragments: MutableList<Fragment> = ArrayList()
    private val mFragmentTitles: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        val fragment = mFragments[position]
        val bundle = Bundle()
        bundle.putSerializable(MOVIE_KEY, movie)
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