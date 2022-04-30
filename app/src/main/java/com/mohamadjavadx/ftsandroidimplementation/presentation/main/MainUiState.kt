package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import com.mohamadjavadx.ftsandroidimplementation.model.Note

data class MainUiState(
    val queries: Set<String> = emptySet(),
    val queryString: String = "",
    val searchOperator: Operator = Operator.AND,
    val isSanitized: Boolean = false,
    val isPhrase: Boolean = true,
    val results: List<Note> = emptyList(),
) {
    enum class Operator {
        AND,
        OR,
    }
}
