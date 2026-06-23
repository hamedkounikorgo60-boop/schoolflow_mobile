package com.schoolflow.mobile.ui.payments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolflow.mobile.api.SchoolFlowRepository
import com.schoolflow.mobile.models.SuiviPaiement
import com.schoolflow.mobile.utils.Resource
import kotlinx.coroutines.launch

class PaymentsViewModel : ViewModel() {

    private val repository = SchoolFlowRepository()

    private val _paiements = MutableLiveData<Resource<SuiviPaiement>>()
    val paiements: LiveData<Resource<SuiviPaiement>> = _paiements

    private val _recuUrl = MutableLiveData<Resource<String>>()
    val recuUrl: LiveData<Resource<String>> = _recuUrl

    fun loadPaiements(eleveId: Int) {
        _paiements.value = Resource.Loading()
        viewModelScope.launch {
            _paiements.value = repository.getPaiements(eleveId)
        }
    }

    fun getRecu(paiementId: Int) {
        _recuUrl.value = Resource.Loading()
        viewModelScope.launch {
            _recuUrl.value = repository.getRecuPaiement(paiementId)
        }
    }
}
