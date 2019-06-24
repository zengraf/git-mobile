package com.commityourself.gitmobile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.commityourself.gitmobile.EXTRA_NAME
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.component
import com.commityourself.gitmobile.utils.NameValidator
import com.commityourself.gitmobile.utils.simplify
import com.commityourself.gitmobile.views.AddRepositoryActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InitActionFragment : Fragment(), AddRepositoryActivity.Listener {
    private lateinit var nameField: TextInputLayout
    private val repository = component.repository()
    private val initSettingsFragment = InitSettingsFragment()
    private var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_init_action, container, false)
        nameField = view.findViewById(R.id.repository_name)

        if (savedInstanceState == null)
            childFragmentManager
                .beginTransaction()
                .add(R.id.settings_fragment_holder, initSettingsFragment)
                .commit()
        else
            name = savedInstanceState.getString(EXTRA_NAME) ?: ""

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
                            initSettingsFragment.changeText(editText?.text.toString())
                        }
                }
            }
            editText?.setText(name)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_NAME, nameField.editText?.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun getEnteredData(intent: Intent): Boolean {
        if (nameField.error != null)
            return false
        if (nameField.editText?.text.toString() == "") {
            nameField.error = "Name must not be empty"
            return false
        }
        intent.putExtra(EXTRA_NAME, nameField.editText?.text.toString())
        return true
    }
}
