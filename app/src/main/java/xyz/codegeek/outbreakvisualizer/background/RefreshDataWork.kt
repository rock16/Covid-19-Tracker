package xyz.codegeek.outbreakvisualizer.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.KoinComponent
import org.koin.core.inject
import xyz.codegeek.outbreakvisualizer.data.repository.Repository
import java.io.IOException
import xyz.codegeek.outbreakvisualizer.helpers.Result as trial

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {
    companion object{
        const val WORK_NAME = "xyz.codegeek.outbreakvisualizer.background.RefreshDataWork"
    }
    val repo: Repository by inject()
    override suspend fun doWork(): Result {
        try {
            val result = repo.fetchremoteData()
            if (result is trial.Success) repo.saveRemoteDataSource(result) else throw IOException("Unable to fetch data")
        } catch (e: IOException) {
            return Result.retry()
        }
        return Result.success()
    }
}