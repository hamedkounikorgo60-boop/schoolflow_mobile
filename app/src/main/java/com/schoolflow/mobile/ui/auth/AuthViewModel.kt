package com.schoolflow.mobile.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolflow.mobile.api.SchoolFlowRepository
import com.schoolflow.mobile.models.LoginResponse
import com.schoolflow.mobile.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = SchoolFlowRepository()

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> = _loginResult

    private val _changePasswordResult = MutableLiveData<Resource<Unit>>()
    val changePasswordResult: LiveData<Resource<Unit>> = _changePasswordResult

    private val _logoutResult = MutableLiveData<Resource<Unit>>()
    val logoutResult: LiveData<Resource<Unit>> = _logoutResult

    fun login(email: String, password: String) {
        _loginResult.value = Resource.Loading()
        viewModelScope.launch {
            _loginResult.value = repository.login(email, password)
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmation: String) {
        _changePasswordResult.value = Resource.Loading()
        viewModelScope.launch {
            _changePasswordResult.value = repository.changePassword(
                currentPassword, newPassword, confirmation
            )
        }
    }

    fun logout() {
        _logoutResult.value = Resource.Loading()
        viewModelScope.launch {
            _logoutResult.value = repository.logout()
        }
    }
}
