package com.commityourself.gitmobile.fragments

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.commityourself.gitmobile.EXTRA_LOCAL_PATH
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.utils.STORAGE
import com.commityourself.gitmobile.utils.simplify
import com.commityourself.gitmobile.views.AddRepositoryActivity
import com.google.android.material.textfield.TextInputLayout

class CloneSettingsFragment : PreferenceFragmentCompat(), AddRepositoryActivity.OnChangeTextListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.clone_action_settings, rootKey)
        findPreference<EditTextPreference>(EXTRA_LOCAL_PATH)?.apply {
            summary = "${STORAGE.canonicalPath}${this.text}/"
        }
    }

    override fun changeText(text: String) {
        findPreference<EditTextPreference>(EXTRA_LOCAL_PATH)?.apply {
            summary = "${STORAGE.canonicalPath}${this.text}/${text.simplify()}"
        }
    }
}