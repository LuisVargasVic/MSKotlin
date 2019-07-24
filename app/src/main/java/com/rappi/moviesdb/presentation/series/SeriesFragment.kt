package com.rappi.moviesdb.presentation.series

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.FragmentSeriesBinding
import com.rappi.moviesdb.domain.Serie
import com.rappi.moviesdb.presentation.MainActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SeriesFragment : Fragment(), SeriesAdapter.SerieClickListener {

    private lateinit var viewDataBinding: FragmentSeriesBinding
    private var typeSelected = SeriesViewModel.SORT_POPULAR
    private lateinit var mAdapter: SeriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentSeriesBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewModel =
            SeriesViewModel(context!!, isNetworkAvailable())

        val layoutManager = GridLayoutManager(context, 4)
        viewDataBinding.rvSeries.layoutManager = layoutManager
        viewDataBinding.rvSeries.setHasFixedSize(true)

        mAdapter = SeriesAdapter(
            viewDataBinding.viewModel?.seriesRepository?.series?.value,
            this
        )
        viewDataBinding.rvSeries.adapter = mAdapter

        viewDataBinding.viewModel?.seriesRepository?.series?.observe(this, Observer {
            // size = it.size
            when (typeSelected) {
                SeriesViewModel.SORT_POPULAR -> mAdapter.setSeriesList(it.createSubList(
                    SeriesViewModel.SORT_POPULAR, 0,20))
                SeriesViewModel.SORT_TOP -> mAdapter.setSeriesList(it.createSubList(
                    SeriesViewModel.SORT_TOP, 0,20))
                else -> mAdapter.setSeriesList(it.createSubList(SeriesViewModel.SORT_UPCOMING, 0,20))
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_popular -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_POPULAR, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.seriesRepository?.series?.value?.createSubList(SeriesViewModel.SORT_POPULAR, 0,20)?.let {
                        mAdapter.setSeriesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SeriesViewModel.SORT_POPULAR
                (context as MainActivity).supportActionBar?.setTitle(R.string.popular)
                true
            }
            R.id.action_top -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_TOP, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.seriesRepository?.series?.value?.createSubList(SeriesViewModel.SORT_TOP, 0,20)?.let {
                        mAdapter.setSeriesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SeriesViewModel.SORT_TOP
                (context as MainActivity).supportActionBar?.setTitle(R.string.top_rated)
                true
            }
            R.id.action_upcoming -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_UPCOMING, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.seriesRepository?.series?.value?.createSubList(SeriesViewModel.SORT_UPCOMING, 0,20)?.let {
                        mAdapter.setSeriesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SeriesViewModel.SORT_UPCOMING
                (context as MainActivity).supportActionBar?.setTitle(R.string.upcoming)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun serieClicked(serie: Serie?) {

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun List<Serie>.createSubList(type: String, from: Int, to: Int): List<Serie> {
        if (this.size >= to) {
            return when (type) {
                SeriesViewModel.SORT_POPULAR -> this.sortedByDescending { it.popularity }.subList(from, to)
                SeriesViewModel.SORT_TOP -> this.sortedByDescending { it.voteAverage }.subList(from, to)
                else -> {
                    val dateTimeStrToLocalDateTime: (Serie) -> Date = {
                        val format: DateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                        format.parse(it.firstAirDate)!!
                    }
                    this.sortedByDescending(dateTimeStrToLocalDateTime).subList(from, to)
                }
            }
        }
        return this
    }

}
