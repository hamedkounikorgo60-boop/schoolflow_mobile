package com.schoolflow.mobile.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolflow.mobile.api.SchoolFlowRepository
import com.schoolflow.mobile.models.Eleve
import com.schoolflow.mobile.utils.Resource
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = SchoolFlowRepository()

    private val _dashboard = MutableLiveData<Resource<Eleve>>()
    val dashboard: LiveData<Resource<Eleve>> = _dashboard

    fun loadDashboard(eleveId: Int) {
        _dashboard.value = Resource.Loading()
        viewModelScope.launch {
            _dashboard.value = repository.getDashboard(eleveId)
        }
    }
}
