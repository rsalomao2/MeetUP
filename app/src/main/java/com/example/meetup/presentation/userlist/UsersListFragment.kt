package com.example.meetup.presentation.userlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetup.R
import com.example.meetup.adapter.DatabaseAdapter
import com.example.meetup.databinding.FragmentUserslistBinding
import com.example.meetup.model.FirestoreUser

class UsersListFragment : Fragment() {

    private lateinit var mAdapter: DatabaseAdapter
    private lateinit var binding: FragmentUserslistBinding
    private lateinit var viewModel: UsersListViewModel

    private val connectionFailureDialog = AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.connectionErrorDialogTitle))
        .setMessage(getString(R.string.connectionErrorDialogMsg))
        .setIcon(R.drawable.ic_error)
        .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_userslist, container, false)
        viewModel = ViewModelProvider(this).get(UsersListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setupFirebaseFirestore()
        setupObservers()
        setupUI()
    }

    private fun setupUI() {
        mAdapter = DatabaseAdapter(mutableListOf()) {
            navigateToUserDetailFragment(it)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }


    private fun navigateToUserDetailFragment(user: FirestoreUser) {
        val action =
            UsersListFragmentDirections.actionRecyclerViewFragmentToUserDetailFragment(user)
        findNavController().navigate(action)
    }

    private fun setupObservers() {
        setupLoadingObserver()
        setupUserListObserver()
        setupConnectionFailureDialogObserver()
    }

    private fun setupConnectionFailureDialogObserver() {
        viewModel.connectionFailureDialogLiveData.observe(viewLifecycleOwner) { show ->
            if (show) {
                connectionFailureDialog.show()
            } else
                connectionFailureDialog.hide()
        }
    }

    private fun setupUserListObserver() {
        viewModel.userListLivedata.observe(viewLifecycleOwner) { users ->
            mAdapter.updateUsers(users)
        }
    }

    private fun setupLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                binding.rvProgressBar.visibility = View.VISIBLE
            } else {
                binding.rvProgressBar.visibility = View.GONE
            }
        }
    }
}

