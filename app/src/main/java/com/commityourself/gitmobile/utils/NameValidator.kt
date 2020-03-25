package com.commityourself.gitmobile.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.commityourself.gitmobile.GitMobile
import com.commityourself.gitmobile.component
import com.commityourself.gitmobile.dagger.annotations.ApplicationScope
import com.commityourself.gitmobile.database.RepositoryRepository
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ApplicationScope
class NameValidator(
    private val text: TextInputLayout,
    private val scope: CoroutineScope
) : TextWatcher {
    private val repository = component.repository()

    override fun afterTextChanged(s: Editable?) {
        scope.launch(Dispatchers.IO) {
            if (checkForErrors())
                withContext(Dispatchers.Main) { text.error = "This name already exists" }
            else
                withContext(Dispatchers.Main) { text.error = null }
        }
    }

    private suspend fun checkForErrors(): Boolean =
        withContext(Dispatchers.Default) { repository.getSimpleNames().contains(text.editText?.text.toString().simplify()) }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}