package xyz.codegeek.outbreakvisualizer.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RegionalDataDoa {
    @Query("SELECT * FROM regionaldata")
    fun loadAllRegion(): List<RegionalData>

    @Query("SELECT * FROM regionaldata WHERE country = :country")
    fun findcountry(country: String): RegionalData

    @Query("SELECT * FROM regionaldata LIMIT 1")
    fun isDatabaseEmpty(): List<RegionalData>

    @Insert
    fun insertRegionList(regions: List<RegionalData>)

    @Query("DELETE FROM regionaldata")
    fun deleteAllData()
}