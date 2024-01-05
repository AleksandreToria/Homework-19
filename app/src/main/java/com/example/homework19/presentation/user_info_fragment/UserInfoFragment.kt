package com.example.homework19.presentation.user_info_fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.homework19.data.common.Resource
import com.example.homework19.databinding.FragmentUserInfoBinding
import com.example.homework19.presentation.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoFragment : BaseFragment<FragmentUserInfoBinding>(FragmentUserInfoBinding::inflate) {

    private val viewModel: UserInfoFragmentViewModel by viewModels()
//    private val userId: Int = 0

    override fun setUp() {
        getUserId()
        getUserData()
    }

    override fun bindViewActionListener() {
    }

    private fun getUserId() {

    }

    private fun getUserData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val userId = arguments?.getInt("userId")

                if (userId != null) {
                    viewModel.getUserInfo(userId)
                    viewModel.userInfoState.collect { userResource ->
                        with(binding) {
                            when (userResource) {
                                is Resource.Success -> {
                                    val user = userResource.data
                                    email.text = user!!.email
                                    firstName.text = user.firstName
                                    lastName.text = user.lastName
                                    setImageResource(user.avatar)
                                }

                                is Resource.Error -> {
                                    // Handle error
                                    // You can show an error message or navigate back
                                }

                                is Resource.Loading -> {
                                    // Handle loading state if needed
                                }

                                else -> ""
                            }
                        }
                    }
                } else {
                    // Handle the case where userId is null
                    // You can show an error message or navigate back
                }
            }
        }
    }


    private fun setImageResource(url: String) {
        Glide.with(this)
            .load(url)
            .into(binding.image)
    }
}
