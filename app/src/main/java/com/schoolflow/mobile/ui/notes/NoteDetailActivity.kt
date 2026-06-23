package com.schoolflow.mobile.ui.notes

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolflow.mobile.R
import com.schoolflow.mobile.SchoolFlowApp
import com.schoolflow.mobile.adapters.NoteDetailAdapter
import com.schoolflow.mobile.databinding.ActivityNoteDetailBinding
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.formatMoyenne
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MATIERE_ID = "matiere_id"
        const val EXTRA_MATIERE_NOM = "matiere_nom"
        const val EXTRA_MATIERE_COEFFICIENT = "matiere_coefficient"
        const val EXTRA_MATIERE_MOYENNE = "matiere_moyenne"
    }

    private lateinit var binding: ActivityNoteDetailBinding
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var noteAdapter: NoteDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val matiereId = intent.getIntExtra(EXTRA_MATIERE_ID, 0)
        val matiereNom = intent.getStringExtra(EXTRA_MATIERE_NOM) ?: ""
        val coefficient = intent.getIntExtra(EXTRA_MATIERE_COEFFICIENT, 0)
        val moyenne = intent.getDoubleExtra(EXTRA_MATIERE_MOYENNE, 0.0)

        binding.toolbar.title = getString(R.string.notes_par_matiere, matiereNom)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.tvNomMatiere.text = matiereNom
        binding.tvCoefficient.text = getString(R.string.coefficient, coefficient)
        binding.tvMoyenneMatiere.text = if (moyenne > 0) moyenne.formatMoyenne() + "/20" else "—"

        setupRecyclerView()
        observeViewModel()

        val eleveId = SchoolFlowApp.instance.sessionManager.getSelectedEleveId()
        viewModel.loadNotes(eleveId, matiereId = matiereId)
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteDetailAdapter()
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@NoteDetailActivity)
            adapter = noteAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.notes.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    noteAdapter.submitList(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                }
            }
        }
    }
}
