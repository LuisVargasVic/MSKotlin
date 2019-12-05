package com.venkonenterprise.mskotlin.presentation.series

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.venkonenterprise.mskotlin.R
import com.venkonenterprise.mskotlin.databinding.MsItemBinding
import com.venkonenterprise.mskotlin.domain.series.Serie
import com.venkonenterprise.mskotlin.presentation.movies.MoviesViewModel
import com.squareup.picasso.Picasso

/**
 * Created by Luis Vargas on 2019-07-24.
 */

class SeriesAdapter(var mSeriesList: List<Serie>?, var mSerieClickListener: SerieClickListener, var mTypeSelected: String) : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    init {
        setHasStableIds(true)
    }

    interface SerieClickListener {
        fun serieClicked(serie: Serie?)
        fun bottom()
        fun hide()
    }

    fun setSeriesList(serieList: List<Serie>) {
        mSeriesList = serieList
    }

    fun setTypeSelected(typeSelected: String) {
        mTypeSelected = typeSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val dataBinding: MsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.ms_item,
            parent,
            false)
        return SeriesViewHolder(dataBinding, mSerieClickListener, mTypeSelected)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(mSeriesList?.get(position))
        if (position == itemCount - 1) {
            mSerieClickListener.bottom()
        } else {
            mSerieClickListener.hide()
        }
    }

    override fun getItemCount(): Int {
        return mSeriesList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class SeriesViewHolder(
        private val msItemBinding: MsItemBinding,
        private val mSerieClickListener: SerieClickListener,
        private val mTypeSelected: String
    ) : RecyclerView.ViewHolder(msItemBinding.root), View.OnClickListener {

        var mSerie: Serie? = null

        init {
            msItemBinding.ivMovieImage.setOnClickListener(this)
        }

        fun bind(serie: Serie?) {
            mSerie = serie
            Picasso
                .get()
                .load(Uri.parse(BASE_URL + serie?.posterPath))
                .into(msItemBinding.ivMovieImage)
            when (mTypeSelected) {
                MoviesViewModel.SORT_POPULAR -> msItemBinding.tvMovieAverage.text = serie?.popularity.toString()
                MoviesViewModel.SORT_TOP -> msItemBinding.tvMovieAverage.text = serie?.voteAverage.toString()
                else -> msItemBinding.tvMovieAverage.text = serie?.firstAirDate.toString()
            }
        }

        override fun onClick(view: View) {
            mSerieClickListener.serieClicked(mSerie)
        }
    }

    companion object {
        private const val BASE_URL = "https://image.tmdb.org/t/p/w185"
    }
}