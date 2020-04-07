package xyz.codegeek.outbreakvisualizer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.codegeek.outbreakvisualizer.helpers.countryFlag
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import kotlinx.coroutines.*
import xyz.codegeek.outbreakvisualizer.api.CovidRegion
import xyz.codegeek.outbreakvisualizer.api.RemoteDataSource
import xyz.codegeek.outbreakvisualizer.data.database.RegionalData
import xyz.codegeek.outbreakvisualizer.data.database.RegionalDataDoa
import xyz.codegeek.outbreakvisualizer.data.database.RegionalMapFeature
import xyz.codegeek.outbreakvisualizer.data.database.RegionalMapFeatureDao
import xyz.codegeek.outbreakvisualizer.helpers.Result

class Repository(
    private val regionalDataDoa: RegionalDataDoa,
    private val regionalFeatureDao: RegionalMapFeatureDao,
    private val remoteDataSource: RemoteDataSource) {

    //private val cachedData = listOf<RegionalData>()

    suspend fun getFeatures(): List<Feature>{
      return extractFeature()
    }

    suspend fun getData(): List<RegionalData> {
        return extractData()
    }

    private suspend fun extractData(): List<RegionalData> {
        val map = mutableMapOf<String, RegionalData>()
        val countryFlagURL = countryFlag
        val countryData = regionalDataDoa.loadAllRegion()
        return withContext(Dispatchers.Default){
            countryData.forEach{
                val value = map[it.country]?: RegionalData(it.region, it.country,0, 0, 0,0)
                value.flag = "https://www.countryflags.io/" + countryFlagURL[it.country] + "/flat/64.png"
                map[it.country] = value.add(it)
            }
            map.values.toList()

        }

    }

    private fun fillCachedFeaturesList(result: Result<List<CovidRegion>>): List<RegionalMapFeature> {
        val cacheFeatures = mutableListOf<RegionalMapFeature>()
        if (result is Result.Success){
            result.data.forEach{covidRegion ->
                covidRegion.data.forEach {
                    val point: Point = Point.fromLngLat(it.long_, it.lat)
                    val feature = Feature.fromGeometry(point)
                    feature.addStringProperty("country", it.country_Region)
                    feature.addNumberProperty("recovered", it.recovered)
                    feature.addNumberProperty("death", it.deaths)
                    feature.addNumberProperty("active",it.active)
                    feature.addNumberProperty("confirmed", it.confirmed)
                    cacheFeatures.add(RegionalMapFeature(feature))
                }
            }
        }
        return cacheFeatures
    }

    private fun cacheData(result: Result<List<CovidRegion>>): List<RegionalData> {
        val cachedData = mutableListOf<RegionalData>()
        if (result is Result.Success){
            result.data.forEach{covidRegion ->
                covidRegion.data.forEach {
                    val regionalData = RegionalData(
                        it.province_State,
                        it.country_Region,
                        it.active,
                        it.deaths,
                        it.recovered,
                        it.confirmed
                    )
                    cachedData.add(regionalData)
                }
            }
        }

        return cachedData
    }


    suspend fun saveRemoteDataSource(result: Result<List<CovidRegion>>) {
        withContext(Dispatchers.IO){
            val regionalFeatureList = async(Dispatchers.Default){fillCachedFeaturesList(result)}
            val regionDataList = async(Dispatchers.Default){cacheData(result)}
            regionalDataDoa.deleteAllData()
            regionalDataDoa.insertRegionList(regionDataList.await())
            regionalFeatureDao.deleteAllFeatures()
            regionalFeatureDao.saveFeatures(regionalFeatureList.await())
        }
    }

    private suspend fun extractFeature(): List<Feature> {
        val featureList = regionalFeatureDao.getFeature()
        val returnedFeatureList = mutableListOf<Feature>()
        withContext(Dispatchers.Default){
            featureList.forEach{
                returnedFeatureList.add(it.mapFeature)
            }
        }
        return returnedFeatureList
    }


    suspend fun shouldUpdateCache(): Boolean {
        if (regionalDataDoa.isDatabaseEmpty().size < 1) {
            return true
        }
        return false
    }

    suspend fun fetchremoteData(): Result<List<CovidRegion>> {
        return remoteDataSource.getCovidRegions()
    }
}