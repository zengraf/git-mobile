package com.commityourself.gitmobile.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.commityourself.gitmobile.EXTRA_ID
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.component
import com.commityourself.gitmobile.fragments.repository.FileBrowserFragment
import com.commityourself.gitmobile.fragments.repository.ReadmeFragment
import com.google.android.material.tabs.TabLayout

class RepositoryActivity : AppCompatActivity() {
    var id: Int = -1
    val repository by lazy { component.repository().repository(id) }
    lateinit var pager: ViewPager

    inner class RepositoryPagerAdapter(fm: FragmentManager, val readme: Boolean) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return if (readme)
                2
            else
                1
        }

        override fun getItem(position: Int): Fragment {
            return if (readme)
                when (position) {
                    0 -> ReadmeFragment().apply { setRepository(this@RepositoryActivity.id) }
                    1 -> FileBrowserFragment().apply { setRoot(repository?.localPath ?: "/") }
                    else -> FileBrowserFragment().apply { setRoot("/") }
                }
            else
                FileBrowserFragment().apply { setRoot(repository?.localPath ?: "/") }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        id = savedInstanceState?.getInt(EXTRA_ID) ?: intent.getIntExtra(EXTRA_ID, -1)

        pager = findViewById(R.id.repository_pager)
        pager.apply {
            adapter = RepositoryPagerAdapter(supportFragmentManager, repository?.readme != null)
        }
        findViewById<TabLayout>(R.id.tabs).apply {
            setupWithViewPager(pager)
            if (repository?.readme == null)
                getTabAt(0)?.setIcon(R.drawable.ic_folder)
            else {
                getTabAt(0)?.setIcon(R.drawable.ic_description)
                getTabAt(1)?.setIcon(R.drawable.ic_folder)
            }
        }

        title = repository?.name ?: resources.getString(R.string.repository_name)
    }
}