package com.rappi.moviesdb.presentation.series

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
    private var title = R.string.popular_series

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {
        const val SERIE_KEY = "serie"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentSeriesBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setTitle(title)
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

        viewDataBinding.viewModel?.seriesRepository?.seriesCategories?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.seriesRepository?.categories?.observe(this, Observer {

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_categories -> {
                val view = layoutInflater.inflate(R.layout.categories, null)
                val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
                for (category in viewDataBinding.viewModel?.seriesRepository?.categories?.value ?: mutableListOf()) {
                    val chip = Chip(chipGroup.context, null, R.style.Choice)
                    chip.isCheckable = true
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewDataBinding.viewModel?.select(category.id)
                        } else {
                            viewDataBinding.viewModel?.deselect(category.id)
                        }
                    }
                    chip.setOnClickListener {}
                    chip.text = category.name
                    chipGroup.addView(chip)
                }
                val alertDialog = AlertDialog.Builder(context!!)
                alertDialog.setView(view)
                alertDialog.setPositiveButton("UPDATE") { dialogInterface: DialogInterface, i: Int ->
                    val series = viewDataBinding.viewModel?.categoriesSelected()!!
                    val seriesSelected = series.createSubList(typeSelected, 0, 20)
                    mAdapter.setSeriesList(seriesSelected)
                    mAdapter.notifyDataSetChanged()
                    dialogInterface.dismiss()
                }
                alertDialog.show()
                true
            }
            R.id.action_popular -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_POPULAR, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.categoriesSelected()?.createSubList(SeriesViewModel.SORT_POPULAR, 0,20)?.let {
                        mAdapter.setSeriesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SeriesViewModel.SORT_POPULAR
                (context as MainActivity).supportActionBar?.setTitle(R.string.popular_series)
                title = R.string.popular_series
                true
            }
            R.id.action_top -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_TOP, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.categoriesSelected()?.createSubList(SeriesViewModel.SORT_TOP, 0,20)?.let {
                        mAdapter.setSeriesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SeriesViewModel.SORT_TOP
                (context as MainActivity).supportActionBar?.setTitle(R.string.top_rated_series)
                title = R.string.top_rated_series
                true
            }
            R.id.action_upcoming -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_UPCOMING, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.categoriesSelected()?.createSubList(SeriesViewModel.SORT_UPCOMING, 0,20)?.let {
                        mAdapter.setSeriesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SeriesViewModel.SORT_UPCOMING
                (context as MainActivity).supportActionBar?.setTitle(R.string.upcoming_series)
                title = R.string.upcoming_series
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun serieClicked(serie: Serie?) {
        val bundle = bundleOf(SERIE_KEY to serie)

        view?.findNavController()?.navigate(
            R.id.action_series_to_serie_detail,
            bundle)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun List<Serie>.createSubList(type: String, from: Int, to: Int): List<Serie> {
        return when (type) {
            SeriesViewModel.SORT_POPULAR -> {
                if (this.size >= to) {
                    this.sortedByDescending { it.popularity }.subList(from, to)
                } else {
                    this.sortedByDescending { it.popularity }
                }
            }
            SeriesViewModel.SORT_TOP -> {
                if (this.size >= to) {
                    this.sortedByDescending { it.voteAverage }.subList(from, to)
                } else {
                    this.sortedByDescending { it.voteAverage }
                }
            }
            else -> {
                val dateTimeStrToLocalDateTime: (Serie) -> Date = {
                    val format: DateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                    format.parse(it.firstAirDate)!!
                }
                if (this.size >= to) {
                    this.sortedByDescending(dateTimeStrToLocalDateTime).subList(from, to)
                } else {
                    this.sortedByDescending(dateTimeStrToLocalDateTime)
                }
            }
        }
    }

}
