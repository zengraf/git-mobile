package com.commityourself.gitmobile.dagger.modules

import com.commityourself.gitmobile.GitMobile
import com.commityourself.gitmobile.dagger.annotations.RepositoryScope
import com.commityourself.gitmobile.utils.openFile
import dagger.Module
import dagger.Provides
import org.eclipse.jgit.api.Git
import javax.inject.Named

@Module
class GitModule {
    @RepositoryScope
    @Provides
    fun provideGit(@Named("path") path: String): Git = Git.open(openFile(path))
}