package app.bettermetesttask.sections.splash

import androidx.lifecycle.ViewModel
import app.bettermetesttask.navigation.HomeCoordinator
import app.bettermetesttask.workers.SyncWorker
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val coordinator: HomeCoordinator
) : ViewModel() {

    fun handleAppLaunch() {
        coordinator.start()
        Timber.i("handleAppLaunch. Sync start")
        SyncWorker.start()
    }
}