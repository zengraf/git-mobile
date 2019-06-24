package com.commityourself.gitmobile.dagger.modules

import com.commityourself.gitmobile.GitMobile
import com.commityourself.gitmobile.dagger.annotations.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(val application: GitMobile) {
    @ApplicationScope
    @Provides
    fun provideApplication(): GitMobile = application
}