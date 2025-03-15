package app.bettermetesttask.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.bettermetesttask.AndroidTestTaskApp
import app.bettermetesttask.datamovies.database.MoviesDatabase
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val moviesRepository: MoviesRepository
): CoroutineWorker(appContext, params) {
    companion object {
        private const val WORK_NAME = "SYNC_MOVIES_WORK"
        private const val TWO_HOURS_IN_SECONDS = 7200
        private const val ONE_HOUR_IN_SECONDS = 3600

        @Volatile
        private var lastSyncTime: Long = 0
        fun checkWorkerState() {
            val currTime = Date().time
            val diff = TimeUnit.MILLISECONDS.toMinutes(currTime - lastSyncTime)
            if (diff > 15) {
                Timber.w("Worker is dead. Trying to restart.")

                start()
            } else Timber.i("Worker is alive.")
        }

        fun start() {
            stop()

            lastSyncTime = Date().time

            val work = OneTimeWorkRequestBuilder<SyncWorker>()
                .setInitialDelay(2, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(AndroidTestTaskApp.appContext).enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, work)

            Timber.i("Initial start.")
        }

        fun stop() {
            Timber.i("Stop.")

            try {
                WorkManager.getInstance(AndroidTestTaskApp.appContext).cancelUniqueWork(WORK_NAME)
            } catch (e: Exception) {
                Timber.e("cancelUniqueWork cancellation error: $e")
            }
        }
    }

    override suspend fun doWork(): Result {
        Timber.i("Sync start.")

        try {
            val database = MoviesDatabase.getDBInstance(applicationContext)

            lastSyncTime = Date().time

            Timber.i("Sync start")
            moviesRepository.refresh()

            Timber.i("Sync end.")
        } catch (e: Exception) {
            Timber.e("Sync error: $e")
        }

        // Here we schedule next work. Delay needed for to success finish current work
        CoroutineScope(Dispatchers.Default).launch {
            delay(300)

            // Night sync interval - 10 minutes, day - 5 minutes
            val nextSyncDelay = if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) in 1..6) 1L else 1L

            val work = OneTimeWorkRequestBuilder<SyncWorker>()
//                .setInitialDelay(nextSyncDelay, TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(AndroidTestTaskApp.appContext).enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, work)

            Timber.i("Next sync scheduled.")
        }

        return Result.success()
    }
}
