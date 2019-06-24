package com.commityourself.gitmobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.database.models.Repository
import com.commityourself.gitmobile.utils.dateToString
import com.pkmmte.view.CircularImageView

class LastCommitFragment(val repository: Repository) : Fragment() {
    val avatar by lazy { view?.findViewById<CircularImageView>(R.id.avatar) }
    val nickname by lazy { view?.findViewById<TextView>(R.id.nickname) }
    val commitMessage by lazy { view?.findViewById<TextView>(R.id.commit_message) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_last_commit, container)
    }

    override fun onStart() {
        super.onStart()
        nickname?.text = repository.lastCommitName
        commitMessage?.text = "${dateToString(repository.lastCommitDate)} — ${repository.lastCommitMessage}"
    }
}