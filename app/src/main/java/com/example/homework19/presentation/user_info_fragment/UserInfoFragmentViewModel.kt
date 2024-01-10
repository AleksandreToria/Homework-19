package com.example.homework19.presentation.user_info_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework19.data.common.Resource
import com.example.homework19.domain.model.GetUser
import com.example.homework19.domain.use_case.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoFragmentViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _userInfoState = MutableStateFlow<Resource<GetUser>>(Resource.Loading(false))
    val userInfoState: StateFlow<Resource<GetUser>?> = _userInfoState.asStateFlow()

    fun getUserInfo(id: Int) = viewModelScope.launch {
        _userInfoState.update { Resource.Loading(false) }

        getUserInfoUseCase.invoke(id).collectLatest { userResource ->
            _userInfoState.update { userResource }
        }
    }
}