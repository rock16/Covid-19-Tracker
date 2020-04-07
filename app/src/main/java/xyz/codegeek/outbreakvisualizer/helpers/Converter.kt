package xyz.codegeek.outbreakvisualizer.helpers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mapbox.geojson.Feature

class FeatureConverter {
    private val gson = Gson()

    @TypeConverter
    fun toFeature(data: String): Feature {
        return Feature.fromJson(data)
    }

    @TypeConverter
    fun fromFeature(feature: Feature): String {
        return gson.toJson(feature)
    }
}