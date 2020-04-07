package xyz.codegeek.outbreakvisualizer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mapbox.geojson.Feature
import xyz.codegeek.outbreakvisualizer.helpers.FeatureConverter

@Entity(tableName = "features")
data class RegionalMapFeature(
    //@TypeConverters(FeatureConverter::class)
    val mapFeature: Feature,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)