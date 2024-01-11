package com.example.homework19.presentation.user_list_fragment

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListFragmentViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _saveData = MutableStateFlow<Resource<List<GetUser>>?>(null)
    val saveData: StateFlow<Resource<List<GetUser>>?> = _saveData.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationFlow: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    private val _userEvents = MutableSharedFlow<UserListFragmentEvents>()
    val userEvents: SharedFlow<UserListFragmentEvents> = _userEvents.asSharedFlow()

    private val selectedUsers = mutableSetOf<Int>()

    private var currentUsers: List<GetUser> = emptyList()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                getUsersUseCase.invoke().collect {
                    when (it) {
                        is Resource.Success -> {
                            currentUsers = it.data
                        }

                        else -> {}
                    }
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
            is UserListFragmentEvents.DeleteUserEvent -> deleteUser()
        }
    }

    private fun deleteUser() {
        viewModelScope.launch {
            val updatedList = currentUsers.filterNot { user -> selectedUsers.contains(user.id) }
            currentUsers = updatedList
            _saveData.value = Resource.Success(updatedList)
            selectedUsers.clear()
        }
    }

    private fun selectUser(id: Int, isSelected: Boolean) {
        viewModelScope.launch {
            if (isSelected) {
                selectedUsers.add(id)
            } else {
                selectedUsers.remove(id)
            }
        }
    }

    private fun navigateUserToDetails(id: Int) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigationToDetails(id))
        }
    }
}

sealed class NavigationEvent {
    data class NavigationToDetails(val id: Int) : NavigationEvent()
}

sealed class UserListFragmentEvents {
    data class SelectUserEvent(val id: Int, val isSelected: Boolean) : UserListFragmentEvents()
    data object DeleteUserEvent : UserListFragmentEvents()
    data class NavigationEvent(val id: Int) : UserListFragmentEvents()
}
