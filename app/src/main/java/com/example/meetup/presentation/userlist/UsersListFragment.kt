package com.example.meetup.presentation.userlist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetup.R
import com.example.meetup.adapter.DatabaseAdapter
import com.example.meetup.databinding.FragmentUserslistBinding
import com.example.meetup.model.FirestoreUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUserslistBinding
    private lateinit var viewModel: UsersListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_userslist, container, false)
        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(UsersListViewModel::class.java)
        viewModel.loadingState.observe(viewLifecycleOwner, { loadingState ->
            if (loadingState == true) {
                binding.rvProgressBar.visibility = View.VISIBLE
            } else {
                binding.rvProgressBar.visibility = View.INVISIBLE
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirebaseFirestore()
    }

    private fun setupFirebaseFirestore() {
        viewModel.showLoading()
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                result.let {
                    val users = ArrayList<FirestoreUser>()
                    for (document in result) {
                        val user = document.toObject<FirestoreUser>()
                        user.let {
                            user.apply {
                                id = document.id
                            }
                            users.add(user)
                        }
                    }
                    showDatabaseData(users)
                    viewModel.hideLoading()
                }
            }
            .addOnFailureListener { exception ->
                connectionFailureDialog()
                Log.w("###", "Error getting documents.", exception)
            }
    }

//    private

    private fun connectionFailureDialog() {
        val connectionFailureDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.connectionErrorDialogTitle))
            .setMessage(getString(R.string.connectionErrorDialogMsg))
            .setIcon(R.drawable.ic_error)
            .create()
        connectionFailureDialog.show()
    }

    private fun showDatabaseData(users: ArrayList<FirestoreUser>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DatabaseAdapter(users) {
                navigateToUserDetailFragment(it)
            }
        }
    }

    private fun navigateToUserDetailFragment(user: FirestoreUser) {
        val action =
            UsersListFragmentDirections.actionRecyclerViewFragmentToUserDetailFragment(user)
        findNavController().navigate(action)
    }
}

