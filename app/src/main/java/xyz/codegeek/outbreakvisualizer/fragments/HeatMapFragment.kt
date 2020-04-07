package xyz.codegeek.outbreakvisualizer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import org.koin.androidx.viewmodel.ext.android.viewModel

import xyz.codegeek.outbreakvisualizer.R
import xyz.codegeek.outbreakvisualizer.helpers.MapboxStyleHelper
import xyz.codegeek.outbreakvisualizer.viewmodels.HeatmapViewModel

/**
 * A simple [Fragment] subclass.
 */
class HeatMapFragment : Fragment(), OnMapReadyCallback {
    val heatmapViewModel: HeatmapViewModel by viewModel()
    private lateinit var mapView: MapView
    private lateinit var adView: AdView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MobileAds.initialize(context)
        adView = requireView().findViewById(R.id.adView_heatmap)
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(inflater.context, getString(R.string.mapbox_access_token))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heat_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.LIGHT, Style.OnStyleLoaded { style ->
            Log.i("HeatMapFragment", "Trying to add feature")
            heatmapViewModel.regionalFeatures.observe(viewLifecycleOwner, Observer {
                MapboxStyleHelper.addSource(style, FeatureCollection.fromFeatures(it))
            })
            MapboxStyleHelper.addHeatmapLayer(style)
            Log.i("HeatMapFragment", "")
        })
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

}
