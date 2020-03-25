package com.commityourself.gitmobile.fragments.repositoryList

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.commityourself.gitmobile.database.RepositoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(private val repository: RepositoryRepository) : ViewModel() {
    val allRepositories = repository.allRepositories

    init {
        viewModelScope.launch { repository.refresh() }
    }
}
