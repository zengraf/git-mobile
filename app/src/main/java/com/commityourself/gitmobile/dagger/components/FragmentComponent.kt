package com.commityourself.gitmobile.dagger.components

import com.commityourself.gitmobile.dagger.annotations.FragmentScope
import com.commityourself.gitmobile.dagger.modules.ViewModelFactoryModule
import com.commityourself.gitmobile.dagger.modules.ViewModelModule
import com.commityourself.gitmobile.fragments.RepositoryListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ViewModelFactoryModule::class, ViewModelModule::class])
interface FragmentComponent {
    fun inject(activity: RepositoryListFragment)
}