package com.example.homework19.presentation.main_fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework19.data.common.Resource
import com.example.homework19.databinding.FragmentMainBinding
import com.example.homework19.presentation.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainFragmentViewModel by viewModels()
    private val adapter = MainFragmentRecyclerAdapter()

    override fun setUp() {
        setUpRecyclerView()
        dataCollect()
    }

    override fun bindViewActionListener() {
        listeners()
    }

    private fun dataCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveData.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val data = resource.data
                            binding.progressBar.isVisible = false
                            adapter.submitList(data)
                        }

                        is Resource.Error -> {
                            binding.progressBar.isVisible = false
                        }

                        is Resource.Loading -> {
                            binding.progressBar.isVisible = resource.isLoading
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun listeners() {
        adapter.setOnItemClickListener { userList ->
            val action =
                MainFragmentDirections.actionMainFragmentToUserInfoFragment(userId = userList.id)
            findNavController().navigate(action)
        }
    }

    private fun setUpRecyclerView() {
        with(binding) {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
        }
    }
}