package xyz.codegeek.outbreakvisualizer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regionaldata")
data class RegionalData(
    //@PrimaryKey(autoGenerate = true)
    val region: String?,
    val country: String,
    val active: Int = 0,
    val death: Int = 0,
    val recovered: Int = 0,
    val confirmed: Int = 0,
    var flag: String? = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
){

    fun add(rData: RegionalData): RegionalData{

        return RegionalData(this.region, this.country, this.active+rData.active, this.death+rData.death,
        this.recovered+rData.recovered, this.confirmed+rData.confirmed, this.flag, rData.id)
    }
}