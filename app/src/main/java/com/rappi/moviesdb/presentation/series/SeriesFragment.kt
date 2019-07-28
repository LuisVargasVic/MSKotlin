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
import com.rappi.moviesdb.domain.series.Serie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.remote.ApiStatus

class SeriesFragment : Fragment(), SeriesAdapter.SerieClickListener {

    private lateinit var viewDataBinding: FragmentSeriesBinding
    private var typeSelected = SeriesViewModel.SORT_POPULAR
    private lateinit var mAdapter: SeriesAdapter
    private var title = R.string.popular_series
    private var actualPage = 1

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
            this,
            typeSelected
        )
        viewDataBinding.rvSeries.adapter = mAdapter

        viewDataBinding.btLoad.setOnClickListener {
            viewDataBinding.viewModel?.seriesRepository?.page?.value?.plus(1)
            if (isNetworkAvailable()) {
                viewDataBinding.viewModel?.sortSeries(typeSelected, isNetworkAvailable(), false)
            }
        }

        actualPage = 1

        viewDataBinding.viewModel?.seriesRepository?.series?.observe(this, Observer {
            if (it.isEmpty()){
                viewDataBinding.tvEmpty.visibility = View.VISIBLE
            } else {
                viewDataBinding.tvEmpty.visibility = View.GONE
            }
            if (viewDataBinding.viewModel?.seriesRepository?.page?.value ?: 1 == 1) {
                when (typeSelected) {
                    SeriesViewModel.SORT_POPULAR -> {
                        mAdapter.setTypeSelected(SeriesViewModel.SORT_POPULAR)
                        viewDataBinding.viewModel?.categoriesSelected(SeriesViewModel.SORT_POPULAR, 0, 20)?.let {
                            mAdapter.setSeriesList(it)
                        }
                    }
                    SeriesViewModel.SORT_TOP -> {
                        mAdapter.setTypeSelected(SeriesViewModel.SORT_TOP)
                        viewDataBinding.viewModel?.categoriesSelected(SeriesViewModel.SORT_TOP, 0, 20)?.let {
                            mAdapter.setSeriesList(it)
                        }
                    }
                    else -> {
                        mAdapter.setTypeSelected(SeriesViewModel.SORT_UPCOMING)
                        viewDataBinding.viewModel?.categoriesSelected(SeriesViewModel.SORT_UPCOMING, 0, 20)?.let {
                            mAdapter.setSeriesList(it)
                        }
                    }
                }
                mAdapter.notifyDataSetChanged()
            } else {
                val oldSize = mAdapter.mSeriesList?.size
                val to = 20 * (viewDataBinding.viewModel?.seriesRepository?.page?.value ?: 0)
                if (actualPage < viewDataBinding.viewModel?.seriesRepository?.page?.value ?: 0) {
                    actualPage = viewDataBinding.viewModel?.seriesRepository?.page?.value ?: 0
                    val seriesSelected = viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0, to)
                    seriesSelected?.let {
                        mAdapter.setSeriesList(it)
                    }
                    if (mAdapter.mSeriesList?.size?.rem(20) ?: 0 == 0
                        && (mAdapter.mSeriesList?.size ?: 0) - 20 == oldSize
                    ) {
                        viewDataBinding.rvSeries.post {
                            mAdapter.notifyItemRangeInserted(
                                (mAdapter.mSeriesList?.size ?: 0) - 20,
                                mAdapter.mSeriesList?.size ?: 0
                            )
                        }
                    } else if (oldSize ?: 0 <= mAdapter.mSeriesList?.size ?: 0) {
                        viewDataBinding.btLoad.visibility = View.VISIBLE
                    }
                } else if (actualPage > viewDataBinding.viewModel?.seriesRepository?.page?.value ?: 0){
                    viewDataBinding.btLoad.visibility = View.VISIBLE
                }
            }
        })

        viewDataBinding.viewModel?.seriesRepository?.seriesCategories?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.seriesRepository?.categories?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.seriesRepository?.page?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.seriesRepository?.apiStatus?.observe(this, Observer {
            if (it == ApiStatus.LOADING) {
                viewDataBinding.tvEmpty.visibility = View.GONE
                viewDataBinding.pbLoad.visibility = View.VISIBLE
                viewDataBinding.pbLoadMore.visibility = View.GONE
                viewDataBinding.rvSeries.visibility = View.GONE
                viewDataBinding.btLoad.visibility = View.GONE
            } else if (it == ApiStatus.APPENDING)  {
                viewDataBinding.pbLoadMore.visibility = View.VISIBLE
                viewDataBinding.pbLoad.visibility = View.GONE
                viewDataBinding.rvSeries.visibility = View.VISIBLE
                viewDataBinding.btLoad.visibility = View.GONE
            } else if (it == ApiStatus.STOP) {
                viewDataBinding.pbLoadMore.visibility = View.GONE
                viewDataBinding.pbLoad.visibility = View.GONE
                viewDataBinding.rvSeries.visibility = View.VISIBLE
            } else {
                viewDataBinding.pbLoadMore.visibility = View.GONE
                viewDataBinding.pbLoad.visibility = View.GONE
                viewDataBinding.rvSeries.visibility = View.VISIBLE
                viewDataBinding.btLoad.visibility = View.VISIBLE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewDataBinding.viewModel?.categoriesSelected?.clear()
        actualPage = 1
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
                    if (isNetworkAvailable()) {
                        viewDataBinding.viewModel?.sortSeries(typeSelected, isNetworkAvailable(), true)
                    }
                    val series = viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0, 20)!!
                    mAdapter = SeriesAdapter(
                        series,
                        this,
                        typeSelected
                    )
                    viewDataBinding.rvSeries.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
                    dialogInterface.dismiss()
                }
                alertDialog.show()
                true
            }
            R.id.action_popular -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_POPULAR, isNetworkAvailable(), true)
                }
                typeSelected = SeriesViewModel.SORT_POPULAR
                mAdapter = SeriesAdapter(
                    viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0,20),
                    this,
                    typeSelected
                )
                viewDataBinding.rvSeries.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
                (context as MainActivity).supportActionBar?.setTitle(R.string.popular_series)
                title = R.string.popular_series
                true
            }
            R.id.action_top -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_TOP, isNetworkAvailable(), true)
                }
                typeSelected = SeriesViewModel.SORT_TOP
                mAdapter = SeriesAdapter(
                    viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0,20),
                    this,
                    typeSelected
                )
                viewDataBinding.rvSeries.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
                (context as MainActivity).supportActionBar?.setTitle(R.string.top_rated_series)
                title = R.string.top_rated_series
                true
            }
            R.id.action_upcoming -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortSeries(SeriesViewModel.SORT_UPCOMING, isNetworkAvailable(), true)
                }
                typeSelected = SeriesViewModel.SORT_UPCOMING
                mAdapter = SeriesAdapter(
                    viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0,20),
                    this,
                    typeSelected
                )
                viewDataBinding.rvSeries.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
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

    override fun bottom() {
        if (isNetworkAvailable()) {
            viewDataBinding.viewModel?.sortSeries(typeSelected, isNetworkAvailable(), false)
        } else {
            val oldSize = mAdapter.mSeriesList?.size
            if (oldSize ?: 0 >= 20) {
                val seriesSelected = viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0, (oldSize ?: 0) + 20)
                seriesSelected?.let {
                    mAdapter.setSeriesList(it)
                }
                if (((oldSize ?: 0) + 20) == mAdapter.mSeriesList?.size) {
                    viewDataBinding.rvSeries.post {
                        mAdapter.notifyItemRangeInserted(
                            (mAdapter.mSeriesList?.size ?: 0) - 20,
                            mAdapter.mSeriesList?.size ?: 0
                        )
                    }
                }
            }
        }
    }

    override fun hide() {
        viewDataBinding.btLoad.visibility = View.GONE
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
