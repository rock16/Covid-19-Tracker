package xyz.codegeek.outbreakvisualizer.helpers

import android.util.Log
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.HeatmapLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import java.net.URI
import java.net.URISyntaxException

object MapboxStyleHelper {

    fun addSource(style: Style, featureCollection: FeatureCollection){
        try {
            style.addSource(GeoJsonSource(SOURCE_ID, featureCollection))
        }catch (uriSyntaxException: URISyntaxException){
            Log.e(uriSyntaxException.toString(), "That's not a url... ")
        }
    }

    fun addHeatmapLayer(style: Style){
        val layer: HeatmapLayer = HeatmapLayer(HEATMAP_LAYER_ID, SOURCE_ID)
        layer.maxZoom = 9f
        layer.sourceLayer = HEATMAP_LAYER_SOURCE
        layer.setProperties(
            heatmapColor(
                interpolate(
                    linear(), heatmapDensity(),
                    literal(0.01), rgba(0, 0, 0, 0.01),
                    literal(0.1), rgba(0, 2, 114, .1),
                    literal(0.2), rgba(0, 6, 219, .15),
                    literal(0.3), rgba(0, 74, 255, .2),
                    literal(0.4), rgba(0, 202, 255, .25),
                    literal(0.5), rgba(73, 255, 154, .3),
                    literal(0.6), rgba(171, 255, 59, .35),
                    literal(0.7), rgba(200, 197, 3, .4),
                    literal(0.8), rgba(230, 82, 1, 0.7),
                    literal(0.9), rgba(196, 0, 1, 0.8),
                    literal(1), rgba(121, 0, 0, 0.8)
                )
            ),
            heatmapIntensity(
                .02f
            ),
            heatmapRadius(
                interpolate(
                    linear(), subtract(toNumber(get("confirmed")), toNumber(get("recovered"))),
                    stop(0, 2),
                    stop(9, 20)
                )
            ),
            heatmapWeight(
                subtract(toNumber(get("confirmed")), toNumber(get("death")))
            ),
            heatmapOpacity(
                interpolate(
                    linear(), zoom(),
                    stop(7, 1),
                    stop(9, 0)
                )
            )
        )
        style.addLayerAbove(layer, "waterway-label")
    }
    fun addCircleLayer(style: Style){
        val circleLayer: CircleLayer = CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID)
        circleLayer.setProperties(
            circleRadius(
                interpolate(
                    linear(), zoom(),
                    literal(7), interpolate(
                        linear(), get("confirmed"),
                        stop(100, 1),
                        stop(500, 5)
                    ),
                    literal(16), interpolate(
                        linear(), get("confirmed"),
                        stop(500, 5),
                        stop(2000, 50)
                    )
                )
            ),

            circleColor(
                interpolate(
                    linear(), get("confirmed"),
                    literal(0.01), rgba(0, 0, 0, 0.01),
                    literal(0.1), rgba(0, 2, 114, .1),
                    literal(0.2), rgba(0, 6, 219, .15),
                    literal(0.3), rgba(0, 74, 255, .2),
                    literal(0.4), rgba(0, 202, 255, .25),
                    literal(0.5), rgba(73, 255, 154, .3),
                    literal(0.6), rgba(171, 255, 59, .35),
                    literal(0.7), rgba(200, 197, 3, .4),
                    literal(0.8), rgba(255, 82, 1, 0.7),
                    literal(0.9), rgba(196, 0, 1, 0.8),
                    literal(0.95), rgba(121, 0, 0, 0.8)
                )
            ),

            circleOpacity(
                interpolate(
                    linear(), zoom(),
                    stop(5, 0),
                    stop(10, 5)
                )
            ),
            circleStrokeColor("white"),
            circleStrokeWidth(.9f)
        )
        style.addLayerBelow(circleLayer, HEATMAP_LAYER_ID)
    }
}