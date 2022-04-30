package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamadjavadx.ftsandroidimplementation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val noteAdapter = NoteAdapter {
            viewModel.deleteNote(it.id)
        }

        val queriesAdapter = QueriesAdapter(
            delete = {
                viewModel.removeQuery(it)
            }
        )

        binding.rvResults.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

        binding.rvQueries.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = queriesAdapter
        }

        binding.rbOr.setOnClickListener {
            viewModel.updateSearchOperator(MainUiState.Operator.OR)
        }

        binding.rbAnd.setOnClickListener {
            viewModel.updateSearchOperator(MainUiState.Operator.AND)
        }

        binding.cbPhrase.setOnClickListener {
            viewModel.updatePhraseState()
        }

        binding.cbSanitize.setOnClickListener {
            viewModel.updateSanitizedState()
        }

        binding.btnAdd.setOnClickListener {
            val value = (binding.etSearch.text).toString().trim()
            if (value.isNotEmpty()) {
                viewModel.addQuery(value)
                binding.etSearch.text = null
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    noteAdapter.submitList(it.results.toList())
                    queriesAdapter.submitList(it.queries.toList())

                    when (it.searchOperator) {
                        MainUiState.Operator.AND -> binding.tgSearchMood.check(binding.rbAnd.id)
                        MainUiState.Operator.OR -> binding.tgSearchMood.check(binding.rbOr.id)
                    }

                    binding.cbPhrase.isEnabled = it.isSanitized
                    binding.cbPhrase.isChecked = it.isPhrase
                    binding.cbSanitize.isChecked = it.isSanitized

                    binding.tvQueryString.text = "queryString: ${it.queryString}"
                }
            }
        }

        binding.btnAddNote.setOnClickListener {
            val tvTitle = EditText(this).apply {
                hint = "title"
            }
            val tvDetails = EditText(this).apply {
                hint = "details"
            }
            val linearLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                addView(tvTitle)
                addView(tvDetails)
            }

            AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(linearLayout)
                .setPositiveButton("SAVE") { _, _ ->
                    val title = tvTitle.text?.toString()?.trim() ?: ""
                    val details = tvDetails.text?.toString()?.trim() ?: ""
                    if (title.isNotEmpty() && details.isNotEmpty()) {
                        viewModel.addNote(title, details)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .show()
        }
    }

}