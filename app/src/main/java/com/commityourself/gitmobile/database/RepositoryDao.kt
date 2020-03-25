package com.commityourself.gitmobile.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.commityourself.gitmobile.database.models.Repository

@Dao
interface RepositoryDao {
    @Query("SELECT * from repository_table WHERE favorite = 'true' ORDER BY last_commit_date ASC")
    fun getFavoriteRepositories(): LiveData<List<Repository>>

    @Query("SELECT * from repository_table ORDER BY last_commit_date ASC")
    fun getAllRepositories(): LiveData<List<Repository>>

    @Query("SELECT name from repository_table")
    suspend fun getAllNames(): List<String>

    @Update
    suspend fun update(repository: Repository)

    @Insert
    suspend fun insert(repository: Repository)

    @Delete
    suspend fun delete(repository: Repository)
}