package com.venkonenterprise.mskotlin.presentation.serie_detail


import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venkonenterprise.mskotlin.R
import com.venkonenterprise.mskotlin.databinding.FragmentSerieDetailBinding
import com.venkonenterprise.mskotlin.domain.series.Serie
import com.venkonenterprise.mskotlin.presentation.series.SeriesFragment.Companion.SERIE_KEY
import com.squareup.picasso.Picasso

class SerieDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentSerieDetailBinding
    private lateinit var serie: Serie

    companion object {
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
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            viewDataBinding.ivDetailSerieBackdrop.visibility = View.GONE
        } else {
            Picasso
                .get()
                .load(Uri.parse(BASE_URL + serie.backdropPath))
                .error(R.drawable.ic_panorama)
                .into(viewDataBinding.ivDetailSerieBackdrop)
        }
        val adapter = SeriePagerAdapter(childFragmentManager, serie)
        adapter.addFragment(SerieSynopsisFragment.newInstance(), getString(R.string.synopsis))
        adapter.addFragment(SerieVideosFragment.newInstance(), getString(R.string.videos))
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SERIE_KEY, serie)
    }


}
