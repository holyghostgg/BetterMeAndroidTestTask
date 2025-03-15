package app.bettermetesttask.injection.modules

import app.bettermetesttask.featurecommon.injection.scopes.FragmentScope
import app.bettermetesttask.sections.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SplashFragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun createSplashFragmentInjector(): SplashFragment

}