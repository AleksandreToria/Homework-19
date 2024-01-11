package com.example.homework19.presentation.user_list_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework19.data.common.Resource
import com.example.homework19.domain.use_case.GetUsersUseCase
import com.example.homework19.presentation.event.UserListFragmentEvents
import com.example.homework19.presentation.state.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListFragmentViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _userListState = MutableStateFlow(UserListState())
    val userListState: StateFlow<UserListState> = _userListState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                getUsersUseCase.invoke().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val userList = resource.data
                            _userListState.update {
                                it.copy(users = userList)
                            }
                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun onEvent(event: UserListFragmentEvents) {
        when (event) {
            is UserListFragmentEvents.SelectUserEvent -> selectUser(event.id, event.isSelected)
            is UserListFragmentEvents.DeleteUserEvent -> deleteUser()
        }
    }

    private fun deleteUser() {
        viewModelScope.launch {
            val updatedUsers =
                _userListState.value.users.filterNot { it.id in _userListState.value.selectedUsers }
            _userListState.value =
                _userListState.value.copy(users = updatedUsers, selectedUsers = emptySet())
        }
    }

    private fun selectUser(id: Int, isSelected: Boolean) {
        viewModelScope.launch {
            val newSelectedUsers = _userListState.value.selectedUsers.toMutableSet()
            if (isSelected) {
                newSelectedUsers.add(id)
            } else {
                newSelectedUsers.remove(id)
            }
            _userListState.value = _userListState.value.copy(selectedUsers = newSelectedUsers)
        }
    }
}