package xyz.codegeek.outbreakvisualizer.api

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface Cov19Service {
    @GET("/v1/covid-19-json")
    suspend fun getCovidData(): Response<List<CovidRegion>>

    companion object{
        val ENDPOINT = "https://outbreak-visualizer.appspot.com"
    }
}