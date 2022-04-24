package com.mohamadjavadx.ftsandroidimplementation.model

import java.util.*

data class Note(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val details: String,
)