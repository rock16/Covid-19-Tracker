package xyz.codegeek.outbreakvisualizer.Di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.codegeek.outbreakvisualizer.api.Cov19Service
import xyz.codegeek.outbreakvisualizer.api.RemoteDataSource
import xyz.codegeek.outbreakvisualizer.data.database.OutbreakDatabase
import xyz.codegeek.outbreakvisualizer.data.repository.Repository

val dataModule = module(override = true) {
    single { RemoteDataSource(get()) }
    single {
        Room.databaseBuilder(androidContext(),
            OutbreakDatabase::class.java, "outbreak-database"
        ).fallbackToDestructiveMigration().build()
    }
    single { Repository(get(), get(),get()) }
    single { get<OutbreakDatabase>().regionalDataDao() }
    single { get<OutbreakDatabase>().regionalMapFeatureDao() }
}

fun provideServiceredundant(gson: Gson): Cov19Service {
    return Retrofit.Builder()
        .baseUrl(Cov19Service.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(Cov19Service::class.java)
}