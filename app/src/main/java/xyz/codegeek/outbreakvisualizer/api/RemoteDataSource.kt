package xyz.codegeek.outbreakvisualizer.api

import xyz.codegeek.outbreakvisualizer.helpers.Result
import xyz.codegeek.outbreakvisualizer.helpers.safeApiCall
import java.io.IOException

class RemoteDataSource(private val service: Cov19Service) {
    /**
     * Get a list of regions affected with covid-19 around the world
     */

    suspend fun getCovidRegions() = safeApiCall(
        call = {makeRequest()},
        errorMessage = "Error fetching Covid data"
    )

    private suspend fun makeRequest(): Result<List<CovidRegion>> {
        val response = service.getCovidData()
        if (response.isSuccessful){
            val body = response.body()
            if (body != null){
                return Result.Success(body)
            }
        }
        return Result.Error(
            IOException("Error fetching Covid data")
        )
    }
}