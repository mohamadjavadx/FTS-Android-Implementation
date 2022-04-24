package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamadjavadx.ftsandroidimplementation.databinding.ItemSearchQueryBinding
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.Condition
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.Condition.Operator.AND
import com.mohamadjavadx.ftsandroidimplementation.ftsHelper.Condition.Operator.OR

class ConditionAdapter(
    private val update: (newCondition: Condition) -> Unit,
    private val delete: (condition: Condition) -> Unit,
) : ListAdapter<Condition, ConditionAdapter.ConditionViewHolder>(
    object : DiffUtil.ItemCallback<Condition>() {
        override fun areItemsTheSame(oldItem: Condition, newItem: Condition): Boolean {
            return oldItem.value == newItem.value && oldItem.operator == newItem.operator
        }

        override fun areContentsTheSame(oldItem: Condition, newItem: Condition): Boolean {
            return oldItem.value == newItem.value && oldItem.operator == newItem.operator
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConditionViewHolder {
        val binding =
            ItemSearchQueryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConditionViewHolder(binding, update, delete)
    }

    override fun onBindViewHolder(holder: ConditionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ConditionViewHolder(
        private val binding: ItemSearchQueryBinding,
        private val update: (newCondition: Condition) -> Unit,
        private val delete: (condition: Condition) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Condition) {
            binding.tvQuery.text = item.sanitizeValue()
            binding.rg.clearChecked()
            when (item.operator) {
                AND -> binding.rg.check(binding.rbAnd.id)
                OR -> binding.rg.check(binding.rbOr.id)
            }
            binding.rbAnd.setOnClickListener {
                update(item.copy(operator = AND))
            }
            binding.rbOr.setOnClickListener {
                update(item.copy(operator = OR))
            }
            binding.btnDelete.setOnClickListener {
                delete(item)
            }
        }

    }

}