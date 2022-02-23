package com.example.meetup.presentation.signin.email

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.meetup.R
import com.example.meetup.databinding.FragmentEmailLoginBinding
import com.example.meetup.extensions.showToast


class EmailLoginFragment : Fragment() {
    private lateinit var binding: FragmentEmailLoginBinding
    private lateinit var viewModel: EmailLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_email_login, container, false)
        viewModel = ViewModelProvider(this).get(EmailLoginViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupObservables()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservables() {
        setupEmailLoginObservable()
        setupEmailValidationObserver()
        setupPasswordValidationObserver()
    }

    private fun setupPasswordValidationObserver() {
        viewModel.isValidPassword.observe(viewLifecycleOwner) { isValidPassword ->
            if (!isValidPassword) {
                binding.emailLoginTextInput.error = viewModel.passwordErrorMessage.toString()
            } else {
                binding.emailLoginTextInput.error = null
                binding.emailLoginTextInput.isErrorEnabled = false
            }
        }
    }

    private fun setupEmailLoginObservable() {
        viewModel.firebaseEmailLoginTask.observe(viewLifecycleOwner) { task ->
            if (task) {
                findNavController().graph.startDestination = R.id.recyclerViewFragment
                findNavController().navigate(findNavController().graph.startDestination)
                // findNavController().navigate(R.id.action_emailLoginFragment_to_recyclerViewFragment)
                Log.d("###", "signInWithEmail:success")
            } else {
                requireContext().showToast("Invalid Email or Password!")
                Log.d("###", "signInWithEmail:failure")
            }
        }
    }

    private fun setupEmailValidationObserver() {
        viewModel.isValidEmail.observe(viewLifecycleOwner) { isValidEmail ->
            if (!isValidEmail) {
                binding.emailLoginTextInput.error = viewModel.emailErrorMessage.toString()
            } else {
                binding.emailLoginTextInput.error = null
                binding.emailLoginTextInput.isErrorEnabled = false
            }
        }
    }

    private fun setupListeners() {
        binding.emailLoginEditText.addTextChangedListener(TextFieldValidation(binding.emailLoginEditText))
        binding.passwordLoginEditText.addTextChangedListener(TextFieldValidation(binding.passwordLoginEditText))
        binding.createNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_emailLoginFragment_to_registrationFragment)
        }
        binding.signinBtn.setOnClickListener {
            if (isValidEmail() and isValidPassword()) {
                signInWithFirebase()
            }
        }
    }

    private fun signInWithFirebase() {
        val password: String = binding.passwordLoginTextInput.editText?.text.toString()
        val email: String = binding.emailLoginTextInput.editText?.text.toString()
        viewModel.signInWithFirebase(email, password)
    }

    private fun isValidPassword(): Boolean {
        val password: String = binding.passwordLoginTextInput.editText?.text.toString()
        return viewModel.isValidPassword(password)
    }

    private fun isValidEmail(): Boolean {
        val email: String = binding.emailLoginTextInput.editText?.text.toString()
        return viewModel.isValidEmail(email)
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.email_login_editText -> isValidEmail()
                R.id.password_login_editText -> isValidPassword()
            }
        }
    }
}
