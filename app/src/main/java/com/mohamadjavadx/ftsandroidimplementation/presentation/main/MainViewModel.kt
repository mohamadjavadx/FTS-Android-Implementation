package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import androidx.lifecycle.*
import com.mohamadjavadx.ftsandroidimplementation.datasource.local.NoteDao
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.Condition
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.ConditionManager
import com.mohamadjavadx.ftsandroidimplementation.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val noteDao: NoteDao,
    private val conditionManager: ConditionManager,
) : ViewModel() {

    private val _refresh = MutableLiveData<Unit>(Unit)
    private val refresh: LiveData<Unit> = _refresh

    fun addNote(title: String, details: String) = noteDao.saveNote(title, details).also {
        _refresh.value = Unit
    }

    fun deleteNote(id: UUID) = noteDao.deleteNote(id).also {
        _refresh.value = Unit
    }

    fun deleteAllNotes() = noteDao.deleteAllNotes().also {
        _refresh.value = Unit
    }

    fun addCondition(condition: Condition) = conditionManager.addOrUpdateCondition(condition).also {
        _refresh.value = Unit
    }

    fun removeCondition(condition: Condition) = conditionManager.removeCondition(condition).also {
        _refresh.value = Unit
    }

    fun clearConditions(condition: Condition) = conditionManager.clearConditions().also {
        _refresh.value = Unit
    }

    val searchResult: LiveData<List<Note>> = Transformations.switchMap(refresh) {
        if (conditionManager.generateQueryString().isEmpty()) {
            noteDao.getAllNotesAsLiveData()
        } else {
            noteDao.searchAsLiveData(conditionManager.generateQueryString())
        }
    }

    val allConditions: LiveData<List<Condition>> = Transformations.switchMap(refresh) {
        liveData {
            emit(conditionManager.allConditions.toList())
        }
    }

}