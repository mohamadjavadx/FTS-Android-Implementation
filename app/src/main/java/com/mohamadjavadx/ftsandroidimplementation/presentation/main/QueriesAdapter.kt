package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamadjavadx.ftsandroidimplementation.databinding.ItemSearchQueryBinding

class QueriesAdapter(
    private val delete: (query: String) -> Unit,
) : ListAdapter<String, QueriesAdapter.ConditionViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConditionViewHolder {
        val binding =
            ItemSearchQueryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConditionViewHolder(binding, delete)
    }

    override fun onBindViewHolder(holder: ConditionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ConditionViewHolder(
        private val binding: ItemSearchQueryBinding,
        private val delete: (query: String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.tvQuery.text = item
            binding.btnDelete.setOnClickListener {
                delete(item)
            }
        }

    }

}