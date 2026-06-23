package com.schoolflow.mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schoolflow.mobile.R
import com.schoolflow.mobile.databinding.ItemAbsenceBinding
import com.schoolflow.mobile.models.Absence
import com.schoolflow.mobile.utils.formatDate

class AbsenceAdapter : ListAdapter<Absence, AbsenceAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAbsenceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemAbsenceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(absence: Absence) {
            binding.tvDate.text = absence.date.formatDate()

            if (absence.motif != null) {
                binding.tvMotif.text = binding.root.context.getString(
                    R.string.motif_label, absence.motif
                )
            } else {
                binding.tvMotif.text = binding.root.context.getString(R.string.motif_non_renseigne)
            }

            if (absence.nombreHeures != null) {
                binding.tvDuree.text = binding.root.context.getString(
                    R.string.heures_format, absence.nombreHeures
                )
            } else {
                binding.tvDuree.text = ""
            }

            if (absence.justifiee) {
                binding.chipJustifiee.text = binding.root.context.getString(R.string.absence_justifiee)
                binding.chipJustifiee.setChipBackgroundColorResource(R.color.success)
                binding.chipJustifiee.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.on_primary)
                )
                binding.viewStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.success)
                )
            } else {
                binding.chipJustifiee.text = binding.root.context.getString(R.string.absence_non_justifiee)
                binding.chipJustifiee.setChipBackgroundColorResource(R.color.warning)
                binding.chipJustifiee.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.on_primary)
                )
                binding.viewStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.warning)
                )
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Absence>() {
        override fun areItemsTheSame(oldItem: Absence, newItem: Absence) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Absence, newItem: Absence) = oldItem == newItem
    }
}
