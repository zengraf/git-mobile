package com.commityourself.gitmobile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commityourself.gitmobile.EXTRA_ID
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.component
import com.commityourself.gitmobile.fragments.repositoryList.RepositoryAdapter
import com.commityourself.gitmobile.fragments.repositoryList.RepositoryListViewModel
import com.commityourself.gitmobile.views.RepositoryActivity
import timber.log.Timber
import javax.inject.Inject

class RepositoryListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: RepositoryListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RepositoryListViewModel::class.java)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.fragmentComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = RepositoryAdapter(context)
        recyclerView = (inflater.inflate(R.layout.repository_list_fragment, container, false) as RecyclerView).apply {
            adapter = this@RepositoryListFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        adapter.listener = object : RepositoryAdapter.Listener {
            override fun onClick(position: Int) {
                val intent = Intent(activity, RepositoryActivity::class.java).apply { putExtra(EXTRA_ID, position) }
                activity?.startActivity(intent)
            }
        }
        return recyclerView
    }

    override fun onStart() {
        super.onStart()
        viewModel.allRepositories.observe(
            this,
            Observer { repositories ->
                repositories
                    .let { adapter.setRepositories(it) }
                    .also { Timber.i("Observed data change") }
            })

        Timber.d("Attempting to read" + viewModel.allRepositories.value?.map { it.toString() }?.fold("") { string, current -> string + "\n" + current })
    }

}
