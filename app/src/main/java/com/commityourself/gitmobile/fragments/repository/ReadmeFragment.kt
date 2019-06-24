package com.commityourself.gitmobile.fragments.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commityourself.gitmobile.R
import com.commityourself.gitmobile.component
import com.commityourself.gitmobile.database.models.Repository
import com.commityourself.gitmobile.utils.openFile
import com.commityourself.gitmobile.utils.readFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import ru.noties.markwon.AbstractMarkwonPlugin
import ru.noties.markwon.Markwon
import ru.noties.markwon.core.CorePlugin
import ru.noties.markwon.ext.tasklist.TaskListPlugin
import ru.noties.markwon.html.HtmlPlugin
import ru.noties.markwon.image.ImagesPlugin
import ru.noties.markwon.recycler.MarkwonAdapter
import java.io.File


class ReadmeFragment : Fragment() {
    private val content: Markwon by lazy {
        Markwon.builder(context!!).apply {
            usePlugins(
                listOf(
                    CorePlugin.create(),
                    HtmlPlugin.create(),
                    ImagesPlugin.create(context!!),
                    TaskListPlugin.create(context!!)
                )
            )
                .usePlugin(
                    object : AbstractMarkwonPlugin() {
                        override fun configureParser(builder: Parser.Builder) {
                            builder.extensions(listOf(TablesExtension.create(), StrikethroughExtension.create()))
                        }
                    }
                )

        }.build()
    }
    private lateinit var recyclerView: RecyclerView
    private val adapter = MarkwonAdapter.createTextViewIsRoot(R.layout.adapter_markdown_entry)
    private var repositoryId = -1
    private val repository: Repository by lazy { component.repository().repository(repositoryId)!! }
    private val readme by lazy { File(openFile(repository.localPath), repository.readme) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_readme, container, false)
        recyclerView = view.findViewById(R.id.readme_content)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            adapter = this@ReadmeFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setRepository(id: Int) {
        repositoryId = id
        lifecycleScope.launch {
            val text = readFile(readme)
            withContext(Dispatchers.Main) {
                adapter.setMarkdown(content, text)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
