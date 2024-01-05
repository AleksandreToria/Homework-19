package com.example.homework19.presentation.main_fragment

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

    override fun bindViewActionListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveData.collect { resource ->
                    val data = when (resource) {
                        is Resource.Success -> resource.data
                        is Resource.Error -> {
                            emptyList()
                        }

                        is Resource.Loading -> {
                            emptyList()
                        }

                        else -> emptyList()
                    }
                    adapter.submitList(data)
                }
            }
        }

        listeners()
    }

    private fun listeners() {
        adapter.setOnItemClickListener { userList ->
            val action =
                MainFragmentDirections.actionMainFragmentToUserInfoFragment(userId = userList.id)
            findNavController().navigate(action)
        }
    }

    override fun setUp() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        with(binding) {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
        }
    }
}