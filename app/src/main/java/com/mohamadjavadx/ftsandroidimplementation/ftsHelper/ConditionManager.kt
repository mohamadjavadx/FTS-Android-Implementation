package com.mohamadjavadx.ftsandroidimplementation.ftsHelper

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ConditionManager @Inject constructor() {

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

    fun clearConditions() {
        conditionsWithOROperator.clear()
        conditionsWithANDOperator.clear()
    }

    fun generateQueryString(): String {
        var queryString = ""

        if (conditionsWithANDOperator.size == 0 && conditionsWithOROperator.size == 0) {
            return ""
        }
        if (conditionsWithANDOperator.size == 0) {
            queryString += generateQueryForORConditions()
        } else if (conditionsWithOROperator.size == 0) {
            queryString += generateQueryForANDConditions()
        } else {
            queryString += generateQueryForANDConditions()
            queryString += " OR (${generateQueryForANDConditions()} AND ${generateQueryForORConditions()})"
        }
        return queryString
    }

    fun generateQueryForORConditions(): String {
        var queryString = "("
        var conditionsCount = 0
        conditionsWithOROperator.forEach { condition ->
            if (conditionsCount != 0) {
                queryString += " OR "
            }
            queryString += "(${condition.sanitizeValue()})"
            conditionsCount++
        }
        queryString += ")"
        return queryString
    }


    fun generateQueryForANDConditions(): String {
        var queryString = "("
        var conditionsCount = 0
        conditionsWithANDOperator.forEach { condition ->
            if (conditionsCount != 0) {
                queryString += " AND "
            }
            queryString += "(${condition.sanitizeValue()})"
            conditionsCount++
        }
        queryString += ")"
        return queryString
    }

}

