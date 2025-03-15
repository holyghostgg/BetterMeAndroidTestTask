package app.bettermetesttask.injection.modules


import app.bettermetesttask.navigation.HomeCoordinator
import app.bettermetesttask.navigation.HomeCoordinatorImpl
import app.bettermetesttask.navigation.HomeNavigator
import app.bettermetesttask.navigation.HomeNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class MainNavigationModule {

    @Binds
    abstract fun bindNavigator(navigatorImpl: HomeNavigatorImpl): HomeNavigator

    @Binds
    abstract fun bindCoordinator(coordinatorImpl: HomeCoordinatorImpl): HomeCoordinator
}