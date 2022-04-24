package com.mohamadjavadx.ftsandroidimplementation.ftsHelper

data class Condition(
    val value: String,
    val operator: Operator,
) {

    fun sanitizeValue(): String {
        val queryWithEscapedQuotes = value.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }

    enum class Operator {
        AND,
        OR,
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Condition

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}