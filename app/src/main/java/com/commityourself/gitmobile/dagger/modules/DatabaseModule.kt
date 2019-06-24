package com.commityourself.gitmobile.dagger.modules

import androidx.room.Room
import com.commityourself.gitmobile.GitMobile
import com.commityourself.gitmobile.dagger.annotations.ApplicationScope
import com.commityourself.gitmobile.database.RepositoryDao
import com.commityourself.gitmobile.database.RepositoryDatabase
import dagger.Module
import dagger.Provides

@Module class DatabaseModule {

    @ApplicationScope
    @Provides
    fun provideDatabase(application: GitMobile): RepositoryDatabase = Room.databaseBuilder(
        application,
        RepositoryDatabase::class.java,
        "repository_database"
    ).fallbackToDestructiveMigration().build()

    @ApplicationScope
    @Provides
    fun provideRepositoryDao(database: RepositoryDatabase): RepositoryDao = database.repositoryDao()

}