package xyz.codegeek.outbreakvisualizer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.codegeek.outbreakvisualizer.helpers.FeatureConverter

@Database(entities = [RegionalData::class, RegionalMapFeature::class], version = 2)
@TypeConverters(FeatureConverter::class)
abstract class OutbreakDatabase: RoomDatabase() {
    abstract fun regionalDataDao(): RegionalDataDoa
    abstract fun regionalMapFeatureDao(): RegionalMapFeatureDao
}