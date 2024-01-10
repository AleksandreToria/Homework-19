package com.example.homework19.presentation.user_list_fragment

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework19.data.common.Resource
import com.example.homework19.domain.model.GetUser
import com.example.homework19.domain.use_case.GetUsersUseCase
import com.example.homework19.presentation.model.SelectableUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListFragmentViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _saveData = MutableStateFlow<Resource<List<GetUser>>?>(null)
    val saveData: StateFlow<Resource<List<GetUser>>?> = _saveData.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<NavigationEvent>()
    val navigationFlow: SharedFlow<NavigationEvent> = _navigationFlow.asSharedFlow()

    private val _userEvents = MutableSharedFlow<UserListFragmentEvents>()
    val userEvents: SharedFlow<UserListFragmentEvents> = _userEvents.asSharedFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                getUsersUseCase.invoke().collect {
                    _saveData.value = it
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun onEvent(event: UserListFragmentEvents) {
        when (event) {
            is UserListFragmentEvents.NavigationEvent -> navigateUserToDetails(event.id)
            is UserListFragmentEvents.SelectUserEvent -> selectUser(event.id, event.isSelected)
            is UserListFragmentEvents.DeleteUserEvent -> deleteUser(event.ids)
        }
    }

    private fun deleteUser(selectedItems: List<SelectableUser>) {
        viewModelScope.launch {
            _saveData.value?.let { resource ->
                if (resource is Resource.Success) {
                    val updatedList = resource.data.map { SelectableUser(it) }.toMutableList()
                    updatedList.removeAll(selectedItems)
                    _saveData.value = Resource.Success(updatedList.map { it.user } ?: emptyList())
                }
            }
        }
    }

    private fun navigateUserToDetails(id: Int) {
        viewModelScope.launch {
            _navigationFlow.emit(NavigationEvent.NavigationToDetails(id))
        }
    }

    private fun selectUser(id: Int, isSelected: Boolean) {
        viewModelScope.launch {
            val currentResource = _saveData.value

            if (currentResource is Resource.Success) {
                val updatedList = currentResource.data.toMutableList()
                val index = updatedList.indexOfFirst { user -> user.id == id }
                if (index != -1) {
                    updatedList[index] = updatedList[index].copy(isSelected = isSelected)
                    _saveData.value = Resource.Success(updatedList)
                    _userEvents.emit(UserListFragmentEvents.SelectUserEvent(id, isSelected))
                }
            }
        }
    }
}

sealed class NavigationEvent {
    data class NavigationToDetails(val id: Int) : NavigationEvent()
}

sealed class UserListFragmentEvents {
    data class SelectUserEvent(val id: Int, val isSelected: Boolean) : UserListFragmentEvents()
    data class DeleteUserEvent(val ids: List<SelectableUser>) : UserListFragmentEvents()
    data class NavigationEvent(val id: Int) : UserListFragmentEvents()
}
