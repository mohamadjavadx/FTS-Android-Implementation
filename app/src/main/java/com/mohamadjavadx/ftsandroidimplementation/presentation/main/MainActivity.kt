package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamadjavadx.ftsandroidimplementation.databinding.ActivityMainBinding
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.Condition
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.Condition.Operator.OR
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val noteAdapter = NoteAdapter {
            mainViewModel.deleteNote(it.id)
        }

        binding.rvResults.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

        mainViewModel.searchResult.observe(this) {
            noteAdapter.submitList(it)
        }

        val conditionAdapter = ConditionAdapter(
            update = {
                mainViewModel.addCondition(it)
            },
            delete = {
                mainViewModel.removeCondition(it)
            }
        )

        binding.rvQueries.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = conditionAdapter
        }

        mainViewModel.allConditions.observe(this) {
            conditionAdapter.submitList(it)
        }

        binding.btnAdd.setOnClickListener {
            val value = (binding.etSearch.text).toString().trim()
            if (value.isNotEmpty()) {
                mainViewModel.addCondition(Condition(value, OR))
                binding.etSearch.text = null
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
                        mainViewModel.addNote(title, details)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .show()
        }


//        mainViewModel.addCondition(Condition("11", OR))
//        mainViewModel.addCondition(Condition("22", OR))
//        mainViewModel.addCondition(Condition("33", OR))
//        mainViewModel.addCondition(Condition("44", OR))
//        mainViewModel.addCondition(Condition("55", OR))
//        mainViewModel.addCondition(Condition("66", OR))
//        mainViewModel.addCondition(Condition("77", OR))
//        mainViewModel.addCondition(Condition("88", OR))
//        mainViewModel.addCondition(Condition("99", OR))

//        mainViewModel.addNote("1","test 1 8 6")
//        mainViewModel.addNote("2","test 2 4 5")
//        mainViewModel.addNote("3","test 3 7 9")
//        mainViewModel.addNote("4","test 4 3 2")
//        mainViewModel.addNote("5","test 5 1 2")
//        mainViewModel.addNote("6","test 6 4 3")
//        mainViewModel.addNote("7","test 7 5 6")
//        mainViewModel.addNote("8","test 8 4 8")
//        mainViewModel.addNote("9","test 9 6 7")


//        mainViewModel.addNote("11", "test 1 88 6")
//        mainViewModel.addNote("22", "test 2 4 55")
//        mainViewModel.addNote("33", "test 3 7 99")
//        mainViewModel.addNote("44", "test 4 3 22")
//        mainViewModel.addNote("55", "test 5 11 2")
//        mainViewModel.addNote("66", "test 6 4 33")
//        mainViewModel.addNote("77", "test 77 5 6")
//        mainViewModel.addNote("88", "test 8 44 8")
//        mainViewModel.addNote("99", "test 9 66 7")

    }

}