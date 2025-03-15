package app.bettermetesttask.injection.modules

import android.app.Application
import android.content.Context
import android.content.res.Resources
import app.bettermetesttask.featurecommon.initializers.AppInitializers
import app.bettermetesttask.initializers.*
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDispatchingAndroidInjector(androidInjector: AndroidInjector<Any>): DispatchingAndroidInjector<Any> {
        return DispatchingAndroidInjector<Any>(androidInjector)
    }

    @Provides
    fun provideAppInitializers(
        timberInitializer: TimberInitializer,
        appDispatchersInitializer: DispatchersInitializer,
        appLifecycleObserversInitializer: AppLifecycleObserversInitializer
    ): AppInitializers {
        return AppInitializers(
            timberInitializer, appDispatchersInitializer, appLifecycleObserversInitializer
        )
    }

    @Provides
    fun provideResources(context: Context): Resources = context.resources
}