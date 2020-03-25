package com.commityourself.gitmobile.dagger.modules

import androidx.lifecycle.ViewModel
import com.commityourself.gitmobile.dagger.annotations.ViewModelKey
import com.commityourself.gitmobile.fragments.repositoryList.RepositoryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RepositoryListViewModel::class)
    abstract fun bindViewModel(repositoryListViewModel: RepositoryListViewModel): ViewModel
}
