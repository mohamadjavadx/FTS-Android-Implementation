package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamadjavadx.ftsandroidimplementation.datasource.local.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val noteDao: NoteDao,
) : ViewModel() {

    private var _state: MutableStateFlow<MainUiState> = MutableStateFlow(
        MainUiState(
            results = noteDao.getAllNotes()
        )
    )
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun addNote(title: String, details: String): UUID {
        return noteDao.saveNote(title, details).also {
            preformSearch()
        }
    }

    fun deleteNote(id: UUID) {
        noteDao.deleteNote(id)
        preformSearch()
    }

    fun addQuery(query: String) {
        _state.update {
            it.copy(queries = it.queries + query)
        }
        preformSearch()
    }

    fun removeQuery(query: String) {
        _state.update {
            it.copy(queries = it.queries - query)
        }
        preformSearch()
    }

    fun updateSearchOperator(operator: MainUiState.Operator) {
        _state.update {
            it.copy(searchOperator = operator)
        }
        preformSearch()
    }

    fun updateSanitizedState() {
        _state.update {
            val isPhrase = if (it.isSanitized) true else it.isPhrase
            it.copy(isSanitized = !it.isSanitized, isPhrase = isPhrase)
        }
        preformSearch()
    }

    fun updatePhraseState() {
        _state.update {
            it.copy(isPhrase = !it.isPhrase)
        }
        preformSearch()
    }

    private fun preformSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            var queryString = ""
            _state.value.queries.forEach { query ->
                if (queryString.isNotEmpty()) {
                    queryString += "${_state.value.searchOperator}"
                }
                queryString += when {
                    _state.value.isPhrase && _state.value.isSanitized -> {
                        "(\"${query}\")"
                    }
                    _state.value.isSanitized -> {
                        "(${query})"
                    }
                    else -> {
                        "(${sanitizeQuery(query)})"
                    }
                }
            }
            _state.update {
                it.copy(
                    queryString = queryString,
                    results = if (it.queries.isEmpty()) noteDao.getAllNotes() else noteDao.search(
                        queryString
                    )
                )
            }
        }
    }

    private fun sanitizeQuery(query: String): String {
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }

}