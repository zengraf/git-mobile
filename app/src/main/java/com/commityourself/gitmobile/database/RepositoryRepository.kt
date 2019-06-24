package com.commityourself.gitmobile.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.commityourself.gitmobile.dagger.annotations.ApplicationScope
import com.commityourself.gitmobile.database.models.Repository
import com.commityourself.gitmobile.utils.simplify
import com.commityourself.gitmobile.utils.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ApplicationScope
class RepositoryRepository @Inject constructor(private val repositoryDao: RepositoryDao) {

    val favoriteRepositories = liveData { emitSource(repositoryDao.getFavoriteRepositories()) }
    val allRepositories: LiveData<List<Repository>> = liveData { emitSource(repositoryDao.getAllRepositories()) }

    suspend fun getNames() = repositoryDao.getAllNames()

    suspend fun getSimpleNames() = getNames().map { it.simplify() }

    fun repository(id: Int) = allRepositories.value?.get(id)

    suspend fun refresh() = allRepositories.value?.forEach { update(it.update()) }

    suspend fun insert(repository: Repository): Repository =
        withContext(Dispatchers.IO) {
            repositoryDao.insert(repository)
            repository
        }

    suspend fun update(repository: Repository) = withContext(Dispatchers.IO) { repositoryDao.update(repository) }

    suspend fun delete(repository: Repository) = withContext(Dispatchers.IO) { repositoryDao.delete(repository) }
}