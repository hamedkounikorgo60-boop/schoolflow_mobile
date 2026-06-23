package com.schoolflow.mobile.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolflow.mobile.R
import com.schoolflow.mobile.adapters.NotificationAdapter
import com.schoolflow.mobile.databinding.FragmentNotificationsBinding
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        viewModel.loadNotifications()
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter { notification ->
            if (!notification.lue) {
                viewModel.markAsRead(notification.id)
            }
        }
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.loadNotifications() }
        binding.swipeRefresh.setColorSchemeResources(R.color.primary)
    }

    private fun observeViewModel() {
        viewModel.notifications.observe(viewLifecycleOwner) { result ->
            binding.swipeRefresh.isRefreshing = false
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvEmpty.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val notifications = result.data ?: emptyList()
                    if (notifications.isEmpty()) {
                        binding.tvEmpty.visible()
                        binding.rvNotifications.gone()
                    } else {
                        binding.tvEmpty.gone()
                        binding.rvNotifications.visible()
                        notificationAdapter.submitList(notifications)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.tvEmpty.text = result.message
                    binding.tvEmpty.visible()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
