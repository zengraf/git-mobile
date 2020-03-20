package com.commityourself.gitmobile.fragments.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.utils.openFile
import kotlinx.coroutines.Dispatchers

class FileBrowserFragment : Fragment(), FileBrowserAdapter.Listener {
    private val filesAdapter: FileBrowserAdapter by lazy { FileBrowserAdapter(context) }
    private lateinit var pathHolder: TextView
    private lateinit var path: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_file_browser, container, false)
        pathHolder = view.findViewById<TextView>(R.id.relative_path).apply {
            text = "/"
            setOnClickListener { filesAdapter.setRoot(openFile(path)) }
        }
        filesAdapter.listener = this
        view.findViewById<RecyclerView>(R.id.files_list).apply {
            adapter = filesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        return view
    }

    fun setRoot(newPath: String) {
        path = newPath
        filesAdapter.setRoot(openFile(path))
    }

    override fun onChangeDirectory(newPath: String) {
        pathHolder.text = newPath
    }
}