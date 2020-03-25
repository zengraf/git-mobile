package com.commityourself.gitmobile.fragments.repositoryList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.database.models.Repository
import com.commityourself.gitmobile.fragments.LastCommitFragment
import com.commityourself.gitmobile.views.MainActivity
import com.google.android.material.card.MaterialCardView

class RepositoryAdapter internal constructor(val context: Context?) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {
    interface Listener {
        fun onClick(position: Int)
    }

    private var repositories = emptyList<Repository>()
    lateinit var listener: Listener

    inner class ViewHolder(val cardView: MaterialCardView) : RecyclerView.ViewHolder(cardView) {
        val title: TextView = cardView.findViewById(R.id.name)
        val description: TextView = cardView.findViewById(R.id.description)
        val favouriteIcon: ImageView = cardView.findViewById(R.id.favourite_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView: MaterialCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_card, parent, false) as MaterialCardView
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]
        holder.title.text = repository.name
        holder.description.text = repository.description
        if (repository.favorite)
            holder.favouriteIcon.visibility = ImageView.VISIBLE
        if (!repository.encrypted && repository.lastCommitName != null) {
            if (context is AppCompatActivity)
                context.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.commit_container,
                        LastCommitFragment(repository)
                    )
                    .commit()
        }
        holder.cardView.setOnClickListener { listener.onClick(position) }
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    internal fun setRepositories(repositories: List<Repository>) {
        this.repositories = repositories
        notifyDataSetChanged()
    }
}