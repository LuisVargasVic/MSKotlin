package com.rappi.moviesdb.presentation.series


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.FragmentSerieDetailBinding
import com.rappi.moviesdb.domain.Serie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.presentation.series.SeriesFragment.Companion.SERIE_KEY
import com.squareup.picasso.Picasso

class SerieDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentSerieDetailBinding
    private lateinit var serie: Serie

    companion object {
        private const val BASE_URL_SMALL = "https://image.tmdb.org/t/p/w342"
        private const val BASE_URL = "https://image.tmdb.org/t/p/w780"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentSerieDetailBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(SERIE_KEY) as Serie
        } else {
            arguments?.getSerializable(SERIE_KEY) as Serie
        }
        setUpUI()
    }

    fun setUpUI() {
        Picasso
            .get()
            .load(Uri.parse(BASE_URL + serie.backdropPath))
            .error(R.drawable.ic_panorama)
            .into(viewDataBinding.ivDetailSerieBackdrop)
        Picasso
            .get()
            .load(Uri.parse(BASE_URL_SMALL + serie.posterPath))
            .error(R.drawable.ic_photo)
            .into(viewDataBinding.ivDetailSerieImage)
        (context as MainActivity).supportActionBar?.title = serie.name
        viewDataBinding.tvDetailSerieVoteAverage.text = serie.voteAverage.toString()
        viewDataBinding.tvDetailSerieFirstAirDate.text = serie.firstAirDate
        viewDataBinding.tvDetailSerieVoteCount.text = serie.voteCount.toString()
        viewDataBinding.tvDetailSeriePopularity.text = serie.popularity.toString()
        viewDataBinding.tvDetailSerieOriginalLanguage.text = serie.originalLanguage
        viewDataBinding.tvDetailSerieOverview.text = serie.overview
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SERIE_KEY, serie)
    }


}
