package com.schoolflow.mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schoolflow.mobile.R
import com.schoolflow.mobile.databinding.ItemRecentNoteBinding
import com.schoolflow.mobile.models.Note
import com.schoolflow.mobile.utils.formatDate
import com.schoolflow.mobile.utils.formatNote

class RecentNoteAdapter : ListAdapter<Note, RecentNoteAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRecentNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.tvMatiere.text = note.matiere?.nom ?: "Matière"
            binding.tvDate.text = note.date?.formatDate() ?: note.trimestreLabel
            binding.tvNote.text = note.valeur.formatNote()

            val colorRes = when {
                note.valeur >= 16 -> R.color.note_excellent
                note.valeur >= 14 -> R.color.note_good
                note.valeur >= 10 -> R.color.note_average
                note.valeur >= 8 -> R.color.note_poor
                else -> R.color.note_fail
            }
            val color = ContextCompat.getColor(binding.root.context, colorRes)
            binding.tvNote.setTextColor(color)
            binding.viewNoteColor.setBackgroundColor(color)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }
}
