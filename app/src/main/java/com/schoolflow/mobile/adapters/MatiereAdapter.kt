package com.schoolflow.mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schoolflow.mobile.R
import com.schoolflow.mobile.databinding.ItemMatiereBinding
import com.schoolflow.mobile.models.Matiere
import com.schoolflow.mobile.utils.formatMoyenne

class MatiereAdapter(
    private val onItemClick: (Matiere) -> Unit
) : ListAdapter<Matiere, MatiereAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMatiereBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matiere = getItem(position)
        holder.bind(matiere)
        holder.itemView.setOnClickListener { onItemClick(matiere) }
    }

    class ViewHolder(private val binding: ItemMatiereBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(matiere: Matiere) {
            binding.tvNomMatiere.text = matiere.nom
            binding.tvCoefficient.text = binding.root.context.getString(
                R.string.coefficient, matiere.coefficient
            )

            if (matiere.moyenne != null) {
                binding.tvMoyenne.text = matiere.moyenne.formatMoyenne() + "/20"
                val colorRes = if (matiere.moyenne >= 10) R.color.success else R.color.error
                binding.tvMoyenne.setTextColor(
                    ContextCompat.getColor(binding.root.context, colorRes)
                )
            } else {
                binding.tvMoyenne.text = "—"
            }

            val nbNotes = matiere.notes?.size ?: 0
            binding.tvNbNotes.text = "$nbNotes note(s)"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Matiere>() {
        override fun areItemsTheSame(oldItem: Matiere, newItem: Matiere) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Matiere, newItem: Matiere) = oldItem == newItem
    }
}
