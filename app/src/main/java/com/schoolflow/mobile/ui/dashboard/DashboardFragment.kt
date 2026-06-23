package com.schoolflow.mobile.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.schoolflow.mobile.R
import com.schoolflow.mobile.SchoolFlowApp
import com.schoolflow.mobile.adapters.RecentNoteAdapter
import com.schoolflow.mobile.databinding.FragmentDashboardBinding
import com.schoolflow.mobile.models.Eleve
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.formatMoyenne
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var recentNoteAdapter: RecentNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChildSelector()
        setupSwipeRefresh()
        observeViewModel()
        loadData()
    }

    private fun setupRecyclerView() {
        recentNoteAdapter = RecentNoteAdapter()
        binding.rvDernieresNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentNoteAdapter
        }
    }

    private fun setupChildSelector() {
        val eleves = SchoolFlowApp.instance.sessionManager.getEleves()
        if (eleves.size > 1) {
            binding.cardChildSelector.visible()
            val names = eleves.map { it.nomComplet }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerChild.adapter = adapter

            val selectedId = SchoolFlowApp.instance.sessionManager.getSelectedEleveId()
            val selectedIndex = eleves.indexOfFirst { it.id == selectedId }
            if (selectedIndex >= 0) {
                binding.spinnerChild.setSelection(selectedIndex)
            }

            binding.spinnerChild.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    SchoolFlowApp.instance.sessionManager.setSelectedEleveId(eleves[position].id)
                    loadData()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { loadData() }
        binding.swipeRefresh.setColorSchemeResources(R.color.primary)
    }

    private fun loadData() {
        val eleveId = SchoolFlowApp.instance.sessionManager.getSelectedEleveId()
        if (eleveId > 0) {
            viewModel.loadDashboard(eleveId)
        }
    }

    private fun observeViewModel() {
        viewModel.dashboard.observe(viewLifecycleOwner) { result ->
            binding.swipeRefresh.isRefreshing = false
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.layoutError.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.layoutError.gone()
                    result.data?.let { displayDashboard(it) }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.layoutError.visible()
                    binding.tvError.text = result.message
                    binding.btnRetry.setOnClickListener { loadData() }
                }
            }
        }
    }

    private fun displayDashboard(eleve: Eleve) {
        binding.tvNomEleve.text = eleve.nomComplet
        binding.tvClasse.text = getString(R.string.classe_label) + " : " + (eleve.classe?.nom ?: "—")
        binding.tvMatricule.text = getString(R.string.matricule_label) + " : " + eleve.matricule

        eleve.photoUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(binding.ivPhoto)
        }

        binding.tvMoyenne.text = eleve.moyenneGenerale?.formatMoyenne()?.plus("/20") ?: "—"

        if (eleve.rang != null && eleve.totalEleves != null) {
            binding.tvRang.text = if (eleve.rang == 1) {
                getString(R.string.rang_premier_format, eleve.totalEleves)
            } else {
                getString(R.string.rang_format, eleve.rang, eleve.totalEleves)
            }
        } else {
            binding.tvRang.text = "—"
        }

        val notes = eleve.dernieresNotes ?: emptyList()
        if (notes.isEmpty()) {
            binding.tvNoRecentNotes.visible()
            binding.rvDernieresNotes.gone()
        } else {
            binding.tvNoRecentNotes.gone()
            binding.rvDernieresNotes.visible()
            recentNoteAdapter.submitList(notes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
