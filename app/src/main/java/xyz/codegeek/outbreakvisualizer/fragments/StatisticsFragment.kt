package xyz.codegeek.outbreakvisualizer.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

import xyz.codegeek.outbreakvisualizer.R
import xyz.codegeek.outbreakvisualizer.adapters.TrendsAdapter
import xyz.codegeek.outbreakvisualizer.databinding.FragmentTrendsBinding
import xyz.codegeek.outbreakvisualizer.viewmodels.TrendsViewModel

/**
 * A simple [Fragment] subclass.
 */
class StatisticsFragment : Fragment() {

    private lateinit var fragmentTrendsBinding: FragmentTrendsBinding
    private lateinit var adView: AdView

    val trendsViewModel: TrendsViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MobileAds.initialize(context)
        adView = requireView().findViewById(R.id.adView_trends)
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navController = findNavController()
        fragmentTrendsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trends, container, false)

        //Collapsible toolbar
        val layout = fragmentTrendsBinding.collapsibleToolbar
        val toolbar = fragmentTrendsBinding.toolBar
        layout.setupWithNavController(toolbar, navController)
        layout.setContentScrimColor(resources.getColor(R.color.white_50))
        layout.setCollapsedTitleTextColor(resources.getColor(R.color.purple_500))
        layout.setExpandedTitleColor(resources.getColor(R.color.white_50))

        // Spinner Observer
        trendsViewModel.spinner.observe(viewLifecycleOwner, Observer { show ->
            fragmentTrendsBinding.spinner.visibility = if(show) View.VISIBLE else View.GONE
        })

        trendsViewModel.snackbar.observe(viewLifecycleOwner, Observer { text ->
            text?.let {
                makeSnackBarShow(text)
            }
        })
        val adapter = TrendsAdapter()
        fragmentTrendsBinding.regionalDataList.adapter = adapter
        trendsViewModel.regionalData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        fragmentTrendsBinding.setLifecycleOwner(this)

        return fragmentTrendsBinding.root
        //return inflater.inflate(R.layout.fragment_trends, container, false)
    }

    private fun makeSnackBarShow(text: String) {
        Snackbar.make(fragmentTrendsBinding.root.rootView, text, 3000).show()
        trendsViewModel.onSnackbarShown()
    }

}
