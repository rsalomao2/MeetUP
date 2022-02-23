package com.example.meetup.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.meetup.R

class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        setupObservers()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun setupObservers() {
        setupCurrentUserObserver()
    }

    private fun setupCurrentUserObserver() {
        viewModel.mAuth.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser == null) {
                findNavController().graph.startDestination = R.id.loginFragment
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                findNavController().graph.startDestination = R.id.recyclerViewFragment
                findNavController().navigate(R.id.action_splashFragment_to_recyclerViewFragment)
            }
        }
    }
}
