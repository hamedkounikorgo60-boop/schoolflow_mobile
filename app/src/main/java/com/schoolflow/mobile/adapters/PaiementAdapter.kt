package com.schoolflow.mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schoolflow.mobile.databinding.ItemPaiementBinding
import com.schoolflow.mobile.models.Paiement
import com.schoolflow.mobile.utils.formatDate
import com.schoolflow.mobile.utils.formatMoney

class PaiementAdapter(
    private val onRecuClick: (Paiement) -> Unit
) : ListAdapter<Paiement, PaiementAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaiementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paiement = getItem(position)
        holder.bind(paiement)
        holder.binding.btnRecu.setOnClickListener { onRecuClick(paiement) }
    }

    class ViewHolder(val binding: ItemPaiementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paiement: Paiement) {
            binding.tvMontant.text = paiement.montant.formatMoney()
            binding.tvDate.text = paiement.datePaiement.formatDate()
            binding.tvModePaiement.text = paiement.modePaiement ?: ""

            if (paiement.recuUrl == null) {
                binding.btnRecu.isEnabled = false
                binding.btnRecu.alpha = 0.5f
            } else {
                binding.btnRecu.isEnabled = true
                binding.btnRecu.alpha = 1f
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Paiement>() {
        override fun areItemsTheSame(oldItem: Paiement, newItem: Paiement) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Paiement, newItem: Paiement) = oldItem == newItem
    }
}
