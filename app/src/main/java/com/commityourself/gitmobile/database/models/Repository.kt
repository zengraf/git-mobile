package com.commityourself.gitmobile.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.commityourself.gitmobile.dagger.components.DaggerRepositoryComponent
import com.commityourself.gitmobile.dagger.components.RepositoryComponent
import java.util.*

@Entity(tableName = "repository_table")
data class Repository(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "local_path") val localPath: String,
    @ColumnInfo(name = "remote_url") var remoteURL: String? = null,
    @ColumnInfo(name = "favorite") var favorite: Boolean = false,
    @ColumnInfo(name = "encrypted") var encrypted: Boolean = false,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "readme") var readme: String? = null,
    @ColumnInfo(name = "last_commit_name") var lastCommitName: String? = null,
    @ColumnInfo(name = "last_commit_email") var lastCommitEmail: String? = null,
    @ColumnInfo(name = "last_commit_date") var lastCommitDate: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = "last_commit_message") var lastCommitMessage: String? = null,
    @ColumnInfo(name = "state") var state: Int = 0,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {

    @Ignore
    private val component: RepositoryComponent = DaggerRepositoryComponent.builder().path(localPath).build()

    @Ignore
    val repository = com.commityourself.gitmobile.component.repository()

    fun git() = component.git()
}