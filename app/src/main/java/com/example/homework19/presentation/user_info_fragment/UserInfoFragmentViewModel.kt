package com.example.homework19.presentation.user_info_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework19.data.common.Resource
import com.example.homework19.domain.user_list.UserList
import com.example.homework19.domain.user_list.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoFragmentViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _userInfoState = MutableStateFlow<Resource<UserList>>(Resource.Loading)
    val userInfoState: StateFlow<Resource<UserList>?> = _userInfoState.asStateFlow()

    fun getUserInfo(id: Int) = viewModelScope.launch {
        _userInfoState.update { Resource.Loading }

        userRepository.getUserInfo(id).collectLatest { userResource ->
            _userInfoState.update { userResource }
        }
    }
}
