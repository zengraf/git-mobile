package com.commityourself.gitmobile.dagger.components

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.commityourself.gitmobile.dagger.modules.DatabaseModule
import com.commityourself.gitmobile.dagger.annotations.ApplicationScope
import com.commityourself.gitmobile.dagger.modules.ApplicationModule
import com.commityourself.gitmobile.dagger.modules.ViewModelModule
import com.commityourself.gitmobile.database.RepositoryRepository
import com.commityourself.gitmobile.fragments.CloneActionFragment
import com.commityourself.gitmobile.fragments.InitActionFragment
import com.commityourself.gitmobile.utils.NameValidator
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DatabaseModule::class, ApplicationModule::class])
interface ApplicationComponent {
    fun repository(): RepositoryRepository

    fun fragmentComponent(): FragmentComponent

    fun inject(fragment: InitActionFragment)
    fun inject(fragment: CloneActionFragment)
}