package xyz.codegeek.outbreakvisualizer.api

import com.google.gson.annotations.SerializedName

data class CovidRegion(
    @SerializedName("data") val data : List<Data>
) data class Data(
    @SerializedName("Province_State") val province_State : String?,
    @SerializedName("Country_Region") val country_Region : String,
    @SerializedName("Lat") val lat : Double = 0.0,
    @SerializedName("Long_") val long_ : Double = 0.0,
    @SerializedName("Confirmed") val confirmed : Int,
    @SerializedName("Deaths") val deaths : Int,
    @SerializedName("Recovered") val recovered : Int,
    @SerializedName("Active") val active : Int
)