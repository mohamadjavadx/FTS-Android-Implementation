package com.mohamadjavadx.ftsandroidimplementation.ftsHelper

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ConditionManager
@Inject
constructor() {

    private val conditionsWithOROperator: MutableSet<Condition> = mutableSetOf()
    private val conditionsWithANDOperator: MutableSet<Condition> = mutableSetOf()
    val allConditions: Set<Condition>
        get() = conditionsWithANDOperator + conditionsWithOROperator

    fun addOrUpdateCondition(condition: Condition) {
        removeCondition(condition)
        when (condition.operator) {
            Condition.Operator.AND -> {
                conditionsWithANDOperator.add(condition)
            }
            Condition.Operator.OR -> {
                conditionsWithOROperator.add(condition)
            }
        }
    }

    fun removeCondition(condition: Condition) {
        conditionsWithANDOperator.remove(condition)
        conditionsWithOROperator.remove(condition)
    }

    fun generateQueryString(): String {
        var queryString = ""
        when {
            conditionsWithANDOperator.isEmpty() && conditionsWithOROperator.isEmpty() -> {
                return queryString
            }
            conditionsWithANDOperator.isEmpty() -> {
                queryString += generateQueryForORConditions()
            }
            conditionsWithOROperator.isEmpty() -> {
                queryString += generateQueryForANDConditions()
            }
            else -> {
                queryString += generateQueryForANDConditions()
                queryString += " OR (${generateQueryForANDConditions()} AND ${generateQueryForORConditions()})"
            }
        }
        return queryString
    }

    private fun generateQueryForORConditions(): String {
        var queryString = "("
        conditionsWithOROperator.forEachIndexed { index, condition ->
            if (index != 0) {
                queryString += " OR "
            }
            queryString += "(${condition.sanitizeValue()})"
        }
        queryString += ")"
        return queryString
    }

    private fun generateQueryForANDConditions(): String {
        var queryString = "("
        conditionsWithANDOperator.forEachIndexed { index, condition ->
            if (index != 0) {
                queryString += " AND "
            }
            queryString += "(${condition.sanitizeValue()})"
        }
        queryString += ")"
        return queryString
    }

}

