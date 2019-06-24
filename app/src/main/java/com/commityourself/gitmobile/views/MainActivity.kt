package com.commityourself.gitmobile.views

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.commityourself.gitmobile.*
import com.commityourself.gitmobile.database.models.Repository
import com.commityourself.gitmobile.fragments.RepositoryListFragment
import com.commityourself.gitmobile.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivityForResult(
                Intent(this@MainActivity, AddRepositoryActivity::class.java),
                ADD_REPOSITORY_REQUEST
            )
        }

        if (!checkPermission())
            requestPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_REPOSITORY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val repository = component.repository()
            val type = data.getIntExtra(EXTRA_TYPE, 0)
            val name = data.getStringExtra(EXTRA_NAME)
            val localPath = data.getStringExtra(EXTRA_LOCAL_PATH) + "/" + name.simplify()
            val favorite = data.getBooleanExtra(EXTRA_FAVOURITE, false)
            val encrypted = data.getBooleanExtra(EXTRA_ENCRYPTED, false)

            when (type) {
                TYPE_CLONE -> {
                    val remote = data.getStringExtra(EXTRA_REMOTE) ?: ""
                    val recursive = data.getBooleanExtra(EXTRA_RECURSIVE, false)

                    GlobalScope.launch {
                        repository.insert(Repository(name, localPath, remote, favorite, encrypted)).apply {
                            clone(recursive)
                                ?.update()
                                ?: delete()
                        }
                    }
                }

                TYPE_INIT -> GlobalScope.launch {
                    repository.insert(Repository(name, localPath, null, favorite, encrypted)).apply {
                        init()
                            ?.update()
                            ?: delete()
                    }
                }

                else -> throw IllegalArgumentException("Bad operation type: $type").also {
                    Timber.e(it, "Bad operation type: $type")
                }
            }
        } else
            Timber.i("Cancelled adding a repository")
    }

    override fun onStart() {
        super.onStart()
    }

    private fun checkPermission(): Boolean {
        if (SDK_INT >= VERSION_CODES.M) {
            val result = checkSelfPermission(WRITE_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun requestPermission() {
        try {
            ActivityCompat.requestPermissions(
                this, arrayOf(WRITE_EXTERNAL_STORAGE),
                WRITE_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    }
}
