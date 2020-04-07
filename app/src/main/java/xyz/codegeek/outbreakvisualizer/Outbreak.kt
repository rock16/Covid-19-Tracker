package xyz.codegeek.outbreakvisualizer

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import xyz.codegeek.outbreakvisualizer.Di.apiModule
import xyz.codegeek.outbreakvisualizer.Di.dataModule
import xyz.codegeek.outbreakvisualizer.Di.uiModule
import xyz.codegeek.outbreakvisualizer.background.RefreshDataWork
import java.util.concurrent.TimeUnit

class Outbreak : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(){
        super.onCreate()
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@Outbreak)
            // modules
            modules(listOf(dataModule, apiModule, uiModule))
        }
        delayedInit()
    }

    /**
     * Setup WorkManager background job to 'fetch' new network twice daily.
     */
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

}