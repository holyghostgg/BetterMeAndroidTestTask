package app.bettermetesttask.injection.modules

import app.bettermetesttask.featurecommon.injection.scopes.ActivityScope
import app.bettermetesttask.movies.injection.MoviesFragmentBuildersModule
import app.bettermetesttask.sections.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeActivityModule {


    @ActivityScope
    @ContributesAndroidInjector(modules = [
        SplashFragmentBuildersModule::class,
        MainNavigationModule::class,
        MoviesFragmentBuildersModule::class,
    ])
    abstract fun createHomeActivityInjector(): HomeActivity

}