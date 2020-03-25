package com.commityourself.gitmobile.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.commityourself.gitmobile.*

import com.commityourself.gitmobile.database.RepositoryRepository
import com.commityourself.gitmobile.utils.NameValidator
import com.commityourself.gitmobile.utils.simplify
import com.commityourself.gitmobile.views.AddRepositoryActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CloneActionFragment : Fragment(), AddRepositoryActivity.Listener {
    private lateinit var nameField: TextInputLayout
    private lateinit var remoteURLField: TextInputLayout
    private val repository = component.repository()
    private val cloneSettingsFragment = CloneSettingsFragment()
    private var name = ""
    private var remoteURL = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clone_action, container, false)
        nameField = view.findViewById(R.id.repository_name)
        remoteURLField = view.findViewById(R.id.remote_url)

        if (savedInstanceState == null)
            childFragmentManager
                .beginTransaction()
                .add(R.id.settings_fragment_holder, cloneSettingsFragment)
                .commit()
        else {
            name = savedInstanceState.getString(EXTRA_NAME) ?: ""
            remoteURL = savedInstanceState.getString(EXTRA_REMOTE) ?: ""
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        nameField.apply {
            editText?.addTextChangedListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (repository.getSimpleNames().contains(editText?.text.toString().simplify()))
                        withContext(Dispatchers.Main) { error = "This name already exists" }
                    else
                        withContext(Dispatchers.Main) {
                            error = null
                            cloneSettingsFragment.changeText(editText?.text.toString())
                        }
                }
            }
            editText?.setText(name)
        }
        remoteURLField.apply {
            editText?.addTextChangedListener {
                if (editText?.text.toString().isNotEmpty() && !URLUtil.isValidUrl(editText?.text.toString()))
                    error = "Invalid URL entered"
                else {
                    error = null
                }
            }
            editText?.setText(remoteURL)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_NAME, nameField.editText?.text.toString())
        outState.putString(EXTRA_REMOTE, remoteURLField.editText?.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun getEnteredData(intent: Intent): Boolean {
        if (nameField.error != null || remoteURLField.error != null)
            return false
        if (nameField.editText?.text.toString() == "") {
            nameField.error = "Name must not be empty"
            return false
        }
        if (remoteURLField.editText?.text.toString() == "") {
            remoteURLField.error = "URL must not be empty"
            return false
        }
        intent.putExtra(EXTRA_NAME, nameField.editText?.text.toString())
        intent.putExtra(EXTRA_REMOTE, remoteURLField.editText?.text.toString())
        return true
    }
}
