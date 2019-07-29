package com.rappi.moviesdb.presentation.serie_detail


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rappi.moviesdb.databinding.FragmentSerieVideosBinding
import com.rappi.moviesdb.domain.series.Serie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.presentation.videos.VideosAdapter
import com.rappi.moviesdb.presentation.series.SeriesFragment

class SerieVideosFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentSerieVideosBinding
    private lateinit var mAdapter: VideosAdapter
    private lateinit var serie: Serie

    companion object {
        @JvmStatic
        fun newInstance(): SerieVideosFragment {
            val args = Bundle()
            val fragment = SerieVideosFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentSerieVideosBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(SeriesFragment.SERIE_KEY) as Serie
        } else {
            arguments?.getSerializable(SeriesFragment.SERIE_KEY) as Serie
        }
        viewDataBinding.viewModel = SerieVideosViewModel(serie.id, context!!, isNetworkAvailable())

        val layoutManager = LinearLayoutManager(context)
        viewDataBinding.rvVideos.layoutManager = layoutManager
        viewDataBinding.rvVideos.setHasFixedSize(true)

        mAdapter = VideosAdapter(
            context!!,
            viewDataBinding.viewModel?.videosSelected(serie.id)
        )
        viewDataBinding.rvVideos.adapter = mAdapter

        viewDataBinding.viewModel?.seriesRepository?.videos?.observe(this, Observer {
            viewDataBinding.viewModel?.videosSelected(serie.id)?.let { videos ->
                mAdapter.setVideosList(videos)
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SeriesFragment.SERIE_KEY, serie)
    }
}
