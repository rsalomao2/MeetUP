package com.example.meetup.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetup.R
import com.example.meetup.adapter.UsersAdapters
import com.example.meetup.databinding.FragmentRecyclerviewBinding
import com.example.meetup.extensions.showToast
import com.example.meetup.model.User
import com.example.meetup.model.UserComplete
import com.example.meetup.model.UserListItem
import com.example.meetup.service.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RecyclerViewFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerviewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNetwork()
       // setupToolbar()
    }

//    private fun setupToolbar() {
//        val toolbar = binding.recyclewViewToolBar
//        toolbar.setupWithNavController(findNavController())
//    }

    private fun setupNetwork() {
        val api = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        api.fetchAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                showLoading()
                val users = response.body() ?: emptyList()
                users.forEach {
                    it.imageUrl = randomUrl()
                }
                showData(users)
                hideLoading()
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                connectionFailureDialog()
            }
        })
    }

    private fun showLoading() {
        binding.rvProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.rvProgressBar.visibility = View.GONE
    }

    private fun randomUrl(): String {
        val listOfUrl = listOf(
            "https://picsum.photos/130?random=$1.jpg",
            "https://picsum.photos/200?random=2.jpg",
            "https://picsum.photos/200?random=3.jpg",
            "https://picsum.photos/200?random=4.jpg",
            "https://picsum.photos/200?random=5.jpg",
            "https://picsum.photos/200?random=6.jpg",
            "https://picsum.photos/200?random=7.jpg",
            "https://picsum.photos/200?random=8.jpg"
        )
        return listOfUrl.random()
    }

    private fun connectionFailureDialog() {
        val connectionFailureDialog = AlertDialog.Builder(requireContext())
            .setTitle("Connection Failure")
            .setMessage("Cannot connect to the API")
            .setIcon(R.drawable.ic_error)
            .create()
        connectionFailureDialog.show()
    }

    private fun showData(users: List<User>) {
        val userListItem: List<UserListItem> = users.map { user ->
            user.convertToUserListItem()
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = UsersAdapters(userListItem) {
                navigateToUserDetailFragment(it)
            }
        }
    }

    private fun createNewUser(it: UserListItem): UserComplete {
        return UserComplete(
            it.name,
            it.userNameText,
            it.emailText,
            it.idText,
            it.imageUrl,
            it.phoneText,
            it.websiteText,
            it.address
        )
    }

    private fun navigateToUserDetailFragment(it: UserListItem) {
        val newUser = createNewUser(it)
        val action =
            RecyclerViewFragmentDirections.actionRecyclerViewFragmentToUserDetailFragment(newUser)
        findNavController().navigate(action)
    }

}

