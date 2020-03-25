package com.commityourself.gitmobile.dagger.modules

import androidx.lifecycle.ViewModelProvider
import com.commityourself.gitmobile.dagger.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}