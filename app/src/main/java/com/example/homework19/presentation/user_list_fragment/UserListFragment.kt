package com.example.homework19.presentation.user_list_fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework19.data.common.Resource
import com.example.homework19.databinding.FragmentUserListBinding
import com.example.homework19.presentation.common.BaseFragment
import com.example.homework19.presentation.model.SelectableUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>(FragmentUserListBinding::inflate) {

    private val viewModel: UserListFragmentViewModel by viewModels()
    private val adapter = UserListRecyclerAdapter()

    override fun setUp() {
        setUpRecyclerView()
        dataCollect()
        setupRemoveButton()
    }

    override fun bindViewActionListener() {
        listeners()
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationFlow.collect { event ->
                    when (event) {
                        is NavigationEvent.NavigationToDetails -> handleNavigation(event.id)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userEvents.collect { event ->
                    when (event) {
                        is UserListFragmentEvents.SelectUserEvent -> handleUserSelect(
                            event.id,
                            event.isSelected
                        )
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupRemoveButton() {
        binding.removeButton.setOnClickListener {
            viewModel.onEvent(UserListFragmentEvents.DeleteUserEvent)
        }
    }

    private fun dataCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveData.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val data = resource.data.map { user -> SelectableUser(user) }
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
            viewModel.onEvent(UserListFragmentEvents.NavigationEvent(userList.id))
        }

        adapter.setOnCheckedChangeListener { position, isChecked ->
            val user = adapter.currentList[position].user
            viewModel.onEvent(UserListFragmentEvents.SelectUserEvent(user.id, isChecked))
        }

        adapter.setRemoveItemsListener {
            viewModel.onEvent(UserListFragmentEvents.DeleteUserEvent)
        }
    }


    private fun handleUserSelect(id: Int, isSelected: Boolean) {
        viewModel.onEvent(UserListFragmentEvents.SelectUserEvent(id, isSelected))

        val updatedList = adapter.currentList.map { selectableUser ->
            if (selectableUser.user.id == id) {
                selectableUser.copy(isSelected = isSelected)
            } else {
                selectableUser
            }
        }

        adapter.submitList(updatedList)
    }

    private fun handleUserRemove(selectedItems: List<SelectableUser>) {
        viewModel.onEvent(UserListFragmentEvents.DeleteUserEvent)
        adapter.removeItems(selectedItems)
    }

    private fun handleNavigation(id: Int) {
        val action =
            UserListFragmentDirections.actionUserListFragmentToUserInfoFragment(userId = id)
        findNavController().navigate(action)
    }

    private fun setUpRecyclerView() {
        with(binding) {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
        }
    }
}