package com.example.meetup.presenter.signup

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.meetup.R
import com.example.meetup.databinding.FragmentRegistrationBinding
import com.example.meetup.util.AutoMask.Companion.mask

class SignUpFragment : Fragment(R.layout.fragment_registration) {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_registration, container, false
        )
        setupListeners()
        setupObservables()
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        return binding.root
    }

    private fun setupObservables() {
        setupFirstNameObserver()
        setupLastNameObserver()
        setupEmailObserver()
        setupPasswordObserver()
        setupPhoneNumberObserver()
        setupCpfObserver()
    }

    private fun setupCpfObserver() {
        viewModel.isValidCpf.observe(viewLifecycleOwner) { isValidCpf ->
            if (!isValidCpf) {
                binding.cpfEditText.error = viewModel.cpfErrorMessage.toString()
            } else {
                binding.cpfTextInputLayout.error = null
                binding.cpfTextInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupPhoneNumberObserver() {
        viewModel.isValidPhoneNumber.observe(viewLifecycleOwner) { isValidPhoneNumber ->
            if (!isValidPhoneNumber) {
                binding.registrationPhoneNumberEditText.error =
                    viewModel.phoneNumberErrorMessage.toString()
            } else {
                binding.phoneNumberTextInputLayout.error = null
                binding.phoneNumberTextInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupPasswordObserver() {
        viewModel.isValidPassword.observe(viewLifecycleOwner) { isValidPassword ->
            if (!isValidPassword) {
                binding.registrationPasswordEditText.error =
                    viewModel.passwordErrorMessage.toString()
            } else {
                binding.registrationPasswordTextInputLayout.error = null
                binding.registrationPasswordTextInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupEmailObserver() {
        viewModel.isValidEmail.observe(viewLifecycleOwner) { isValidEmail ->
            if (!isValidEmail) {
                binding.registrationEmailTextInputLayout.error =
                    viewModel.emailErrorMessage.toString()
            } else {
                binding.registrationEmailTextInputLayout.error = null
                binding.registrationEmailTextInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupLastNameObserver() {
        viewModel.isValidLastName.observe(viewLifecycleOwner) { isValidLastName ->
            if (!isValidLastName) {
                binding.lastNameTextInputLayout.error = viewModel.lastNameErrorMessage.toString()
            } else {
                binding.lastNameTextInputLayout.error = null
                binding.lastNameTextInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupFirstNameObserver() {
        viewModel.isValidFirstName.observe(viewLifecycleOwner) { isValidFirstName ->
            if (!isValidFirstName) {
                binding.firstNameTextInputLayout.error = viewModel.firstNameErrorMessage.toString()
            } else {
                binding.firstNameTextInputLayout.error = null
                binding.firstNameTextInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupListeners() {
        binding.registrationPasswordEditText.addTextChangedListener(TextFieldValidation(binding.registrationPasswordEditText))
        binding.registrationEmailEditText.addTextChangedListener(TextFieldValidation(binding.registrationEmailEditText))
        binding.registrationFirstNameEditText.addTextChangedListener(TextFieldValidation(binding.registrationFirstNameEditText))
        binding.registrationLastNameEditText.addTextChangedListener(TextFieldValidation(binding.registrationLastNameEditText))
        binding.cpfEditText.addTextChangedListener(TextFieldValidation(binding.cpfEditText))
        binding.cpfEditText.addTextChangedListener(mask("###.###.###-##", binding.cpfEditText))
        binding.registrationPhoneNumberEditText.addTextChangedListener(
            mask(
                "(##)#####-####",
                binding.registrationPhoneNumberEditText
            )
        )
        binding.registrationPhoneNumberEditText.addTextChangedListener(TextFieldValidation(binding.registrationPhoneNumberEditText))
        binding.registerButton.setOnClickListener {
            if (isValidEmail() and isValidPassword() and isValidPhoneNumber() and isValidCpf() and isValidFirstName() and isValidLastName()) {
                saveData()
                findNavController().popBackStack()
            }
        }
    }

    private fun isValidFirstName(): Boolean {
        val firstName = binding.firstNameTextInputLayout.editText?.text.toString()
        return viewModel.isValidFirstName(firstName)
    }

    private fun isValidLastName(): Boolean {
        val lastName = binding.lastNameTextInputLayout.editText?.text.toString()
        return viewModel.isValidLastName(lastName)
    }

    private fun isValidEmail(): Boolean {
        val email: String = binding.registrationEmailEditText.text.toString()
        return viewModel.isValidEmail(email)
    }

    private fun isValidPassword(): Boolean {
        val password: String = binding.registrationPasswordEditText.text.toString()
        return viewModel.isValidPassword(password)
    }

    private fun isValidPhoneNumber(): Boolean {
        val phoneNumber = binding.phoneNumberTextInputLayout.editText?.text.toString()
        return viewModel.isValidPhoneNumber(phoneNumber)
    }

    private fun isValidCpf(): Boolean {
        val cpf: String = binding.cpfEditText.text.toString()
        return viewModel.isValidCpf(cpf)
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(inputText: CharSequence?, start: Int, before: Int, count: Int) {

            when (view.id) {
                R.id.registrationEmailEditText -> isValidEmail()
                R.id.registrationPasswordEditText -> isValidPassword()
                R.id.registrationFirstNameEditText -> isValidFirstName()
                R.id.registrationLastNameEditText -> isValidLastName()
                R.id.cpfEditText -> isValidCpf()
                R.id.registrationPhoneNumberEditText -> isValidPhoneNumber()
            }
        }
    }

    private fun createFirebaseUser() {
        val password = binding.registrationPasswordTextInputLayout.editText?.text.toString()
        val email = binding.registrationEmailTextInputLayout.editText?.text.toString()
        viewModel.createFirebaseUser(email, password)
    }

    private fun saveData() {
        val firstName = binding.firstNameTextInputLayout.editText?.text.toString()
        val lastName = binding.lastNameTextInputLayout.editText?.text.toString()
        val email = binding.registrationEmailTextInputLayout.editText?.text.toString()
        val phoneNumber = binding.phoneNumberTextInputLayout.editText?.text.toString()
        val cpf = binding.cpfEditText.text.toString()

        createFirebaseUser()
        viewModel.saveUserToDb(firstName, lastName, email, phoneNumber, cpf)

        val sharedPreferences =
            requireActivity().getSharedPreferences(email, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.apply {
            putString("PHONE_NUMBER", phoneNumber)
            putString("FIRST_NAME", firstName)
            putString("LAST_NAME", lastName)
            putString("EMAIL", email)
            putString("CPF", cpf)
        }.apply()

        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
    }
}
