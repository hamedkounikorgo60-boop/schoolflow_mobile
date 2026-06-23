package com.schoolflow.mobile.ui.payments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolflow.mobile.R
import com.schoolflow.mobile.SchoolFlowApp
import com.schoolflow.mobile.adapters.PaiementAdapter
import com.schoolflow.mobile.databinding.FragmentPaymentsBinding
import com.schoolflow.mobile.models.SuiviPaiement
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.formatMoney
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class PaymentsFragment : Fragment() {

    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentsViewModel by viewModels()
    private lateinit var paiementAdapter: PaiementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
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
        paiementAdapter = PaiementAdapter { paiement ->
            paiement.recuUrl?.let { url ->
                val intent = Intent(requireContext(), PaymentReceiptActivity::class.java).apply {
                    putExtra(PaymentReceiptActivity.EXTRA_RECU_URL, url)
                }
                startActivity(intent)
            }
        }
        binding.rvPaiements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paiementAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { loadData() }
        binding.swipeRefresh.setColorSchemeResources(R.color.primary)
    }

    private fun loadData() {
        val eleveId = SchoolFlowApp.instance.sessionManager.getSelectedEleveId()
        if (eleveId > 0) {
            viewModel.loadPaiements(eleveId)
        }
    }

    private fun observeViewModel() {
        viewModel.paiements.observe(viewLifecycleOwner) { result ->
            binding.swipeRefresh.isRefreshing = false
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvEmpty.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    result.data?.let { displayPaiements(it) }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.tvEmpty.text = result.message
                    binding.tvEmpty.visible()
                }
            }
        }
    }

    private fun displayPaiements(suivi: SuiviPaiement) {
        binding.tvScolariteTotale.text = suivi.scolariteTotale.formatMoney()
        binding.tvTotalPaye.text = suivi.totalPaye.formatMoney()
        binding.tvResteAPayer.text = suivi.resteAPayer.formatMoney()

        val pourcentage = if (suivi.scolariteTotale > 0) {
            ((suivi.totalPaye / suivi.scolariteTotale) * 100).toInt()
        } else 0

        binding.progressPaiement.progress = pourcentage
        binding.tvPourcentage.text = "$pourcentage% payé"

        if (suivi.resteAPayer <= 0) {
            binding.tvResteAPayer.text = getString(R.string.paiement_complet)
            binding.tvResteAPayer.setTextColor(resources.getColor(R.color.success, null))
        }

        if (suivi.paiements.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvPaiements.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvPaiements.visible()
            paiementAdapter.submitList(suivi.paiements)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
