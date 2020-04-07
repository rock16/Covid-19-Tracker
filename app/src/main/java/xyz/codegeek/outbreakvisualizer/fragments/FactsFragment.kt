package xyz.codegeek.outbreakvisualizer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import xyz.codegeek.outbreakvisualizer.R
import xyz.codegeek.outbreakvisualizer.databinding.FragmentFactsBinding

/**
 * A simple [Fragment] subclass.
 */
class FactsFragment : Fragment() {
    private lateinit var factsBinding: FragmentFactsBinding
    private lateinit var adView: AdView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MobileAds.initialize(context)
        adView = requireView().findViewById(R.id.adView_facts)
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
        factsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_facts, container, false)
        //Collapsible toolbar
        val layout = factsBinding.collapsibleToolbarFacts
        val toolbar = factsBinding.toolBarFacts
        layout.setupWithNavController(toolbar, navController)
        layout.setContentScrimColor(resources.getColor(R.color.white_50))
        layout.setCollapsedTitleTextColor(resources.getColor(R.color.purple_500))
        layout.setExpandedTitleColor(resources.getColor(R.color.white_50))
        return factsBinding.root
    }

}
