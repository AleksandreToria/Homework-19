package com.example.homework19.presentation.user_list_fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework19.databinding.FragmentUserListBinding
import com.example.homework19.presentation.common.BaseFragment
import com.example.homework19.presentation.event.UserListFragmentEvents
import com.example.homework19.presentation.model.SelectableUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>(FragmentUserListBinding::inflate) {

    private val viewModel: UserListFragmentViewModel by viewModels()
    private val adapter = UserListRecyclerAdapter()

    override fun setUp() {
        setUpRecyclerView()
        setupRemoveButton()
    }

    override fun bindViewActionListener() {
        adapter.setOnItemClickListener { user ->
            handleNavigation(user.id)
        }

        adapter.setOnCheckedChangeListener { position, isChecked ->
            val user = adapter.currentList[position].user
            viewModel.onEvent(UserListFragmentEvents.SelectUserEvent(user.id, isChecked))
        }
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userListState.collect { state ->
                    val selectableUsers = state.users.map { user ->
                        SelectableUser(user, state.selectedUsers.contains(user.id))
                    }
                    adapter.submitList(selectableUsers)

                    binding.progressBar.isVisible = false

                }
            }
        }
    }

    private fun setupRemoveButton() {
        binding.removeButton.setOnClickListener {
            viewModel.onEvent(UserListFragmentEvents.DeleteUserEvent)
        }
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

