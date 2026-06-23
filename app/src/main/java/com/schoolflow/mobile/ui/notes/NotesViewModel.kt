package com.schoolflow.mobile.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolflow.mobile.api.SchoolFlowRepository
import com.schoolflow.mobile.models.Matiere
import com.schoolflow.mobile.models.MoyenneTrimestrielle
import com.schoolflow.mobile.models.Note
import com.schoolflow.mobile.utils.Resource
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val repository = SchoolFlowRepository()

    private val _matieres = MutableLiveData<Resource<List<Matiere>>>()
    val matieres: LiveData<Resource<List<Matiere>>> = _matieres

    private val _moyennes = MutableLiveData<Resource<List<MoyenneTrimestrielle>>>()
    val moyennes: LiveData<Resource<List<MoyenneTrimestrielle>>> = _moyennes

    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val notes: LiveData<Resource<List<Note>>> = _notes

    fun loadMatieres(eleveId: Int) {
        _matieres.value = Resource.Loading()
        viewModelScope.launch {
            _matieres.value = repository.getMatieres(eleveId)
        }
    }

    fun loadMoyennes(eleveId: Int) {
        _moyennes.value = Resource.Loading()
        viewModelScope.launch {
            _moyennes.value = repository.getMoyennesTrimestrielles(eleveId)
        }
    }

    fun loadNotes(eleveId: Int, trimestre: Int? = null, matiereId: Int? = null) {
        _notes.value = Resource.Loading()
        viewModelScope.launch {
            _notes.value = repository.getNotes(eleveId, trimestre, matiereId)
        }
    }
}
