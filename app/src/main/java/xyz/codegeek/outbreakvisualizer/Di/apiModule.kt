package xyz.codegeek.outbreakvisualizer.Di

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.codegeek.outbreakvisualizer.api.Cov19Service

val apiModule = module (override = true) {
    single { provideOkhttpClient() }
    single { provideRetrofit(get()) }
    single { provideService(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(Cov19Service.ENDPOINT).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkhttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideService(retrofit: Retrofit): Cov19Service = retrofit.create(Cov19Service::class.java)