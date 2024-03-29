package com.example.homework19.presentation.main_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework19.data.common.Resource
import com.example.homework19.domain.user_list.UserList
import com.example.homework19.domain.user_list.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _saveData = MutableStateFlow<Resource<List<UserList>>?>(null)
    val saveData: StateFlow<Resource<List<UserList>>?> = _saveData.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                userRepository.getUsers().collect {
                    _saveData.value = it
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}