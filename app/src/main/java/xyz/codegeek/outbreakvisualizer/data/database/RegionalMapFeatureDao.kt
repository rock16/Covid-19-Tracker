package xyz.codegeek.outbreakvisualizer.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegionalMapFeatureDao {
    @Query("SELECT * FROM features")
    fun getFeature(): List<RegionalMapFeature>

    @Insert
    fun saveFeatures(features: List<RegionalMapFeature>)

    @Query("DELETE FROM features")
    fun deleteAllFeatures()
}