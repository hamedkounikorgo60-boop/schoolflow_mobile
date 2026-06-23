package com.schoolflow.mobile.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolflow.mobile.api.SchoolFlowRepository
import com.schoolflow.mobile.models.Notification
import com.schoolflow.mobile.utils.Resource
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val repository = SchoolFlowRepository()

    private val _notifications = MutableLiveData<Resource<List<Notification>>>()
    val notifications: LiveData<Resource<List<Notification>>> = _notifications

    fun loadNotifications() {
        _notifications.value = Resource.Loading()
        viewModelScope.launch {
            _notifications.value = repository.getNotifications()
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.marquerCommeLue(notificationId)
        }
    }
}
