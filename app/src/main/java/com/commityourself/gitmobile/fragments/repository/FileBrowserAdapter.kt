package com.commityourself.gitmobile.fragments.repository

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.utils.STORAGE
import com.commityourself.gitmobile.utils.sizeToString
import java.io.File

class FileBrowserAdapter internal constructor(val context: Context?) :
    RecyclerView.Adapter<FileBrowserAdapter.ViewHolder>() {
    private var root = STORAGE
    private var file = root
    private var filesList = file.listFiles()
        set(value) {
            field = value.apply {
                sortBy { it.name }
                sortByDescending { it.isDirectory }
            }
        }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val fileIcon: ImageView = view.findViewById(R.id.file_icon)
        val fileName: TextView = view.findViewById(R.id.file_name)
        val fileDescription: TextView = view.findViewById(R.id.file_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_file_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = filesList[position]
        holder.fileIcon.setImageResource(
            when {
                file.isDirectory -> R.drawable.ic_folder
                else -> R.drawable.ic_file
            }
        )
        holder.fileName.text = file.name
        holder.fileDescription.text = when {
            file.isDirectory -> "${file.list().size} items"
            else -> file.length().sizeToString()
        }
        holder.view.setOnClickListener {
            if (filesList[position].isDirectory) {
                this.file = filesList[position]
                filesList = this.file.listFiles()
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return file.list().size
    }

    internal fun setRoot(root: File) {
        this.root = root
        file = root
        filesList = file.listFiles()
        notifyDataSetChanged()
    }
}