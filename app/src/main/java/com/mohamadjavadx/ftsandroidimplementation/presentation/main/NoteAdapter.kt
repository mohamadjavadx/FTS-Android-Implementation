package com.mohamadjavadx.ftsandroidimplementation.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamadjavadx.ftsandroidimplementation.databinding.ItemNoteBinding
import com.mohamadjavadx.ftsandroidimplementation.model.Note

class NoteAdapter(
    private val delete: (note: Note) -> Unit,
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(
    object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding, delete)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class NoteViewHolder(
        private val binding: ItemNoteBinding,
        private val delete: (note: Note) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            binding.tvId.text = item.id.toString()
            binding.tvTitle.text = item.title
            binding.tvDetails.text = item.details

            binding.materialCardView.setOnLongClickListener {
                delete(item)
                true
            }
        }

    }

}