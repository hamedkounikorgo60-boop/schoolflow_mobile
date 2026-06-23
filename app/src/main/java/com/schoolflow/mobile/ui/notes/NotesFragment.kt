package com.schoolflow.mobile.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.schoolflow.mobile.R
import com.schoolflow.mobile.SchoolFlowApp
import com.schoolflow.mobile.adapters.MatiereAdapter
import com.schoolflow.mobile.databinding.FragmentNotesBinding
import com.schoolflow.mobile.models.MoyenneTrimestrielle
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.formatMoyenne
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var matiereAdapter: MatiereAdapter
    private var selectedTrimestre: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        loadData()
    }

    private fun setupTabs() {
        binding.tabTrimestre.addTab(binding.tabTrimestre.newTab().setText("Tous"))
        binding.tabTrimestre.addTab(binding.tabTrimestre.newTab().setText("1er Trim."))
        binding.tabTrimestre.addTab(binding.tabTrimestre.newTab().setText("2ème Trim."))
        binding.tabTrimestre.addTab(binding.tabTrimestre.newTab().setText("3ème Trim."))

        binding.tabTrimestre.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedTrimestre = if (tab.position == 0) null else tab.position
                loadData()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView() {
        matiereAdapter = MatiereAdapter { matiere ->
            val intent = Intent(requireContext(), NoteDetailActivity::class.java).apply {
                putExtra(NoteDetailActivity.EXTRA_MATIERE_ID, matiere.id)
                putExtra(NoteDetailActivity.EXTRA_MATIERE_NOM, matiere.nom)
                putExtra(NoteDetailActivity.EXTRA_MATIERE_COEFFICIENT, matiere.coefficient)
                putExtra(NoteDetailActivity.EXTRA_MATIERE_MOYENNE, matiere.moyenne ?: 0.0)
            }
            startActivity(intent)
        }
        binding.rvMatieres.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = matiereAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { loadData() }
        binding.swipeRefresh.setColorSchemeResources(R.color.primary)
    }

    private fun loadData() {
        val eleveId = SchoolFlowApp.instance.sessionManager.getSelectedEleveId()
        if (eleveId > 0) {
            viewModel.loadMatieres(eleveId)
            viewModel.loadMoyennes(eleveId)
        }
    }

    private fun observeViewModel() {
        viewModel.matieres.observe(viewLifecycleOwner) { result ->
            binding.swipeRefresh.isRefreshing = false
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvEmpty.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val matieres = result.data ?: emptyList()
                    if (matieres.isEmpty()) {
                        binding.tvEmpty.visible()
                        binding.rvMatieres.gone()
                    } else {
                        binding.tvEmpty.gone()
                        binding.rvMatieres.visible()
                        matiereAdapter.submitList(matieres)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.tvEmpty.text = result.message
                    binding.tvEmpty.visible()
                }
            }
        }

        viewModel.moyennes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { displayMoyennes(it) }
                }
                else -> {}
            }
        }
    }

    private fun displayMoyennes(moyennes: List<MoyenneTrimestrielle>) {
        binding.layoutMoyennes.removeAllViews()
        if (moyennes.isEmpty()) {
            binding.cardMoyennes.gone()
            return
        }
        binding.cardMoyennes.visible()

        for (moyenne in moyennes) {
            val itemView = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setPadding(8, 8, 8, 8)
            }

            val labelView = TextView(requireContext()).apply {
                text = moyenne.trimestreLabel.replace("Trimestre", "Trim.")
                textSize = 11f
                gravity = Gravity.CENTER
                setTextColor(resources.getColor(R.color.on_surface_secondary, null))
            }

            val valueView = TextView(requireContext()).apply {
                text = moyenne.moyenne.formatMoyenne() + "/20"
                textSize = 16f
                gravity = Gravity.CENTER
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(
                    if (moyenne.moyenne >= 10) resources.getColor(R.color.success, null)
                    else resources.getColor(R.color.error, null)
                )
            }

            itemView.addView(labelView)
            itemView.addView(valueView)

            if (moyenne.mention != null) {
                val mentionView = TextView(requireContext()).apply {
                    text = moyenne.mention
                    textSize = 10f
                    gravity = Gravity.CENTER
                    setTextColor(resources.getColor(R.color.on_surface_secondary, null))
                }
                itemView.addView(mentionView)
            }

            binding.layoutMoyennes.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
