package com.schoolflow.mobile.ui.absences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolflow.mobile.R
import com.schoolflow.mobile.SchoolFlowApp
import com.schoolflow.mobile.adapters.AbsenceAdapter
import com.schoolflow.mobile.databinding.FragmentAbsencesBinding
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class AbsencesFragment : Fragment() {

    private var _binding: FragmentAbsencesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AbsencesViewModel by viewModels()
    private lateinit var absenceAdapter: AbsenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbsencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        loadData()
    }

    private fun setupRecyclerView() {
        absenceAdapter = AbsenceAdapter()
        binding.rvAbsences.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = absenceAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { loadData() }
        binding.swipeRefresh.setColorSchemeResources(R.color.primary)
    }

    private fun loadData() {
        val eleveId = SchoolFlowApp.instance.sessionManager.getSelectedEleveId()
        if (eleveId > 0) {
            viewModel.loadAbsences(eleveId)
        }
    }

    private fun observeViewModel() {
        viewModel.absences.observe(viewLifecycleOwner) { result ->
            binding.swipeRefresh.isRefreshing = false
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvEmpty.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val absences = result.data ?: emptyList()
                    binding.tvTotalAbsences.text = getString(R.string.total_absences, absences.size)

                    if (absences.isEmpty()) {
                        binding.tvEmpty.visible()
                        binding.rvAbsences.gone()
                    } else {
                        binding.tvEmpty.gone()
                        binding.rvAbsences.visible()
                        absenceAdapter.submitList(absences)
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
