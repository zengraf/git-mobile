package com.commityourself.gitmobile.fragments.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.utils.openFile

class FileBrowserFragment : Fragment() {
    private val filesAdapter: FileBrowserAdapter by lazy { FileBrowserAdapter(context) }
    lateinit var path: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_file_browser, container, false)
        view.findViewById<RecyclerView>(R.id.files_list).apply {
            adapter = filesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        return view
    }

    fun setRoot(path: String) {
        filesAdapter.setRoot(openFile(path))
        this.path = path
    }
}