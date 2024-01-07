package com.example.homework19.presentation.user_info_fragment

import androidx.core.view.isVisible
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

    override fun setUp() {
        getUserData()
    }

    override fun bindViewActionListener() {
    }

    private fun getUserData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val userId = arguments?.getInt("userId")

                if (userId != null) {
                    viewModel.getUserInfo(userId)
                    viewModel.userInfoState.collect { userResource ->
                        when (userResource) {
                            is Resource.Success -> {
                                val user = userResource.data
                                setData(user!!.email, user.firstName, user.lastName)
                                setImageResource(user.avatar)
                                binding.progressBar.isVisible = false
                            }

                            is Resource.Error -> {
                                binding.progressBar.isVisible = false
                            }

                            is Resource.Loading -> {
                                binding.progressBar.isVisible = userResource.isLoading
                            }

                            else -> {}
                        }
                    }

                }
            }
        }
    }

    private fun setImageResource(url: String) {
        Glide.with(this)
            .load(url)
            .into(binding.image)
    }

    private fun setData(emailInput: String, firstNameInput: String, lastNameInput: String) {
        with(binding) {
            email.text = emailInput
            firstName.text = firstNameInput
            lastName.text = lastNameInput
        }
    }
}
