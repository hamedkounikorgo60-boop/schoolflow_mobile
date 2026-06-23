package com.schoolflow.mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schoolflow.mobile.R
import com.schoolflow.mobile.databinding.ItemNotificationBinding
import com.schoolflow.mobile.models.Notification
import com.schoolflow.mobile.utils.formatDateTime
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class NotificationAdapter(
    private val onItemClick: (Notification) -> Unit
) : ListAdapter<Notification, NotificationAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
        holder.itemView.setOnClickListener { onItemClick(notification) }
    }

    class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.tvTitre.text = notification.titre
            binding.tvMessage.text = notification.message
            binding.tvDate.text = notification.date.formatDateTime()
            binding.chipType.text = notification.typeLabel

            val iconRes = when (notification.type) {
                "examen" -> R.drawable.ic_school
                "reunion" -> R.drawable.ic_person
                "paiement" -> R.drawable.ic_payment
                "annonce" -> R.drawable.ic_announcement
                else -> R.drawable.ic_notifications
            }
            binding.ivIcon.setImageResource(iconRes)

            val chipColorRes = when (notification.type) {
                "examen" -> R.color.info
                "reunion" -> R.color.accent
                "paiement" -> R.color.warning
                "annonce" -> R.color.primary
                else -> R.color.on_surface_secondary
            }
            binding.chipType.setChipBackgroundColorResource(chipColorRes)
            binding.chipType.setTextColor(
                ContextCompat.getColor(binding.root.context, R.color.on_primary)
            )

            if (!notification.lue) {
                binding.viewUnread.visible()
                binding.tvTitre.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                binding.viewUnread.gone()
                binding.tvTitre.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification) = oldItem == newItem
    }
}
