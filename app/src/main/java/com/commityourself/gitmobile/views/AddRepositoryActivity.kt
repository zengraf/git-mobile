package com.commityourself.gitmobile.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.commityourself.gitmobile.*
import com.commityourself.gitmobile.fragments.CloneActionFragment
import com.commityourself.gitmobile.fragments.InitActionFragment

import kotlinx.android.synthetic.main.activity_add_repository.*

class AddRepositoryActivity : AppCompatActivity() {
    private val spinner: Spinner by lazy { findViewById<Spinner>(R.id.toolbar_spinner) }
    private var favourite = false
    private val cloneActionFragment = CloneActionFragment()
    private val initActionFragment = InitActionFragment()
    private var currentType = TYPE_CLONE
    private var actionFragment: Fragment = cloneActionFragment

    interface Listener {
        fun getEnteredData(intent: Intent): Boolean
    }

    interface OnChangeTextListener {
        fun changeText(text: String)
    }

    private fun completeAction(): Boolean {
        val data = Intent()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        data.putExtra(EXTRA_TYPE, currentType)
        data.putExtra(EXTRA_FAVOURITE, favourite)
        data.putExtra(EXTRA_LOCAL_PATH, sharedPreferences.getString(EXTRA_LOCAL_PATH, "/Repositories"))
        data.putExtra(EXTRA_ENCRYPTED, sharedPreferences.getBoolean(EXTRA_ENCRYPTED, false))
        if (currentType == TYPE_CLONE)
            data.putExtra(EXTRA_RECURSIVE, sharedPreferences.getBoolean(EXTRA_RECURSIVE, false))
        if (!(actionFragment as Listener).getEnteredData(data))
            return false

        setResult(Activity.RESULT_OK, data)
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_repository)
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.action_container, actionFragment)
                .commit()

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.actions_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = this
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (currentType != position) {
                    currentType = position
                    actionFragment = when (currentType) {
                        TYPE_CLONE -> cloneActionFragment
                        TYPE_INIT -> initActionFragment
                        else -> cloneActionFragment
                    }
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.action_container, actionFragment)
                        .commit()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_repository_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.favourite -> {
                favourite = true
                true
            }
            R.id.complete -> completeAction()
            else -> super.onOptionsItemSelected(item)
        }
    }
}
