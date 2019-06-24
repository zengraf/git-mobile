package com.commityourself.gitmobile.dagger.components

import com.commityourself.gitmobile.GitMobile
import com.commityourself.gitmobile.dagger.annotations.RepositoryScope
import com.commityourself.gitmobile.dagger.modules.GitModule
import dagger.BindsInstance
import dagger.Component
import org.eclipse.jgit.api.Git
import javax.inject.Named

@RepositoryScope
@Component(modules = [GitModule::class])
interface RepositoryComponent {
    fun git(): Git

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun path(@Named("path") path: String): Builder

        fun build(): RepositoryComponent
    }
}