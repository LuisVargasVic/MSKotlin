package com.venkonenterprise.mskotlin.presentation.serie_detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venkonenterprise.mskotlin.R
import com.venkonenterprise.mskotlin.databinding.FragmentSerieSynopsisBinding
import com.venkonenterprise.mskotlin.domain.series.Serie
import com.venkonenterprise.mskotlin.presentation.MainActivity
import com.venkonenterprise.mskotlin.presentation.series.SeriesFragment
import com.squareup.picasso.Picasso

class SerieSynopsisFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentSerieSynopsisBinding
    private lateinit var serie: Serie

    companion object {
        private const val BASE_URL_SMALL = "https://image.tmdb.org/t/p/w342"

        @JvmStatic
        fun newInstance(): SerieSynopsisFragment {
            val args = Bundle()
            val fragment = SerieSynopsisFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentSerieSynopsisBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        serie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(SeriesFragment.SERIE_KEY) as Serie
        } else {
            arguments?.getSerializable(SeriesFragment.SERIE_KEY) as Serie
        }
        super.onViewCreated(view, savedInstanceState)
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
        outState.putSerializable(SeriesFragment.SERIE_KEY, serie)
    }

}
