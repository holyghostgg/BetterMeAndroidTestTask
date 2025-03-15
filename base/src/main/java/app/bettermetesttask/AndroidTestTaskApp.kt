package app.bettermetesttask

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import app.bettermetesttask.featurecommon.initializers.AppInitializers
import dagger.android.DispatchingAndroidInjector
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AndroidTestTaskApp : Application(), Configuration.Provider {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var appInitializers: AppInitializers
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        appInitializers.init(this)
    }
}