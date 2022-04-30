package com.mohamadjavadx.ftsandroidimplementation.datasource.local

import com.couchbase.lite.*
import com.mohamadjavadx.ftsandroidimplementation.model.Note
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDao
@Inject
constructor(
    private val database: Database
) {

    init {
        createIndex()
    }

    private fun createIndex() {
        database.createIndex(
            "${Note::class.simpleName}FTSIndex",
            IndexBuilder.fullTextIndex(
                FullTextIndexItem.property(Note::title.name),
                FullTextIndexItem.property(Note::details.name)
            ).ignoreAccents(false)
        )
    }

    fun saveNote(title: String, details: String): UUID {
        val id = UUID.randomUUID()
        database.save(
            MutableDocument(id.toString()).apply {
                setString(Note::title.name, title)
                setString(Note::details.name, details)
            },
            ConcurrencyControl.LAST_WRITE_WINS
        )
        return id
    }

    fun deleteNote(id: UUID): Boolean {
        val result = runCatching {
            val document = database.getDocument(id.toString())!!
            database.delete(document)
        }
        return result.isSuccess
    }

    fun getAllNotes(): List<Note> {
        val query: Query = database.createQuery(
            """
            SELECT _id, ${Note::details.name}, ${Note::title.name}
            FROM _
            """
        )
        return query.execute().allResults().map {
            Note(
                id = UUID.fromString(it.getString(Note::id.name)!!),
                title = it.getString(Note::title.name)!!,
                details = it.getString(Note::details.name)!!
            )
        }
    }

    fun search(queryString: String): List<Note> {
        val query: Query = database.createQuery(
            """
            SELECT _id, ${Note::details.name}, ${Note::title.name}
            FROM _ 
            WHERE MATCH(${Note::class.simpleName}FTSIndex, '$queryString')
            ORDER BY RANK(${Note::class.simpleName}FTSIndex) DESC
            """
        )
        return query.execute().allResults().map {
            Note(
                id = UUID.fromString(it.getString(Note::id.name)!!),
                title = it.getString(Note::title.name)!!,
                details = it.getString(Note::details.name)!!
            )
        }
    }

}