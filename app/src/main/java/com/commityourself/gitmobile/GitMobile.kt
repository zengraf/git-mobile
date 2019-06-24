package com.commityourself.gitmobile

import android.app.Application
import com.commityourself.gitmobile.dagger.components.ApplicationComponent
import com.commityourself.gitmobile.dagger.components.DaggerApplicationComponent
import com.commityourself.gitmobile.dagger.modules.ApplicationModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

const val TAG = "Git Mobile"
const val EXTRA_TYPE = "type"
const val EXTRA_NAME = "name"
const val EXTRA_REMOTE = "remote"
const val EXTRA_LOCAL_PATH = "local_path"
const val EXTRA_FAVOURITE = "favourite"
const val EXTRA_RECURSIVE = "recursive"
const val EXTRA_ENCRYPTED = "encrypted"
const val EXTRA_ID = "id"
const val ADD_REPOSITORY_REQUEST = 0
const val GET_DIRECTORY_REQUEST = 1
const val WRITE_STORAGE_PERMISSION_REQUEST_CODE = 0x4
const val TYPE_CLONE = 0
const val TYPE_INIT = 1
lateinit var component: ApplicationComponent

class GitMobile : Application() {
    init {
        Timber.plant(Timber.DebugTree())
        component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }
}