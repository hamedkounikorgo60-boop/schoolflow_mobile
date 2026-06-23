package com.schoolflow.mobile.ui.absences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolflow.mobile.api.SchoolFlowRepository
import com.schoolflow.mobile.models.Absence
import com.schoolflow.mobile.utils.Resource
import kotlinx.coroutines.launch

class AbsencesViewModel : ViewModel() {

    private val repository = SchoolFlowRepository()

    private val _absences = MutableLiveData<Resource<List<Absence>>>()
    val absences: LiveData<Resource<List<Absence>>> = _absences

    fun loadAbsences(eleveId: Int) {
        _absences.value = Resource.Loading()
        viewModelScope.launch {
            _absences.value = repository.getAbsences(eleveId)
        }
    }
}
