package com.example.meetup.fragments

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meetup.R
import com.example.meetup.databinding.FragmentRegistrationBinding
import com.example.meetup.util.AutoMask.Companion.mask
import com.example.meetup.util.CpfValidator

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_registration, container, false
        )
        setupToolbar()
        setupListeners()
        return binding.root
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
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.registrationFragmentToolBar
        toolbar.setupWithNavController(findNavController())
    }

    private fun isValidFirstName(): Boolean {
        val firstName = binding.firstNameTextInputLayout.editText?.text.toString()
        return if (firstName.isEmpty()) {
            binding.firstNameTextInputLayout.error = getString(R.string.erro_field_cannot_be_empty)
            false
        } else {
            binding.firstNameTextInputLayout.error = null
            binding.firstNameTextInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun isValidLastName(): Boolean {
        val lastName = binding.lastNameTextInputLayout.editText?.text.toString()
        return if (lastName.isEmpty()) {
            binding.lastNameTextInputLayout.error = getString(R.string.erro_field_cannot_be_empty)
            false
        } else {
            binding.lastNameTextInputLayout.error = null
            binding.lastNameTextInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun isValidEmail(): Boolean {

        val email: String = binding.registrationEmailTextInputLayout.editText?.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return when {
            email.isEmpty() -> {
                binding.registrationEmailTextInputLayout.error =
                    getString(R.string.erro_field_cannot_be_empty)
                false
            }
            !email.matches(emailPattern.toRegex()) -> {
                binding.registrationEmailTextInputLayout.error =
                    getString(R.string.erro_invalid_email_adress)
                false
            }
            else -> {
                binding.registrationEmailTextInputLayout.error = null
                binding.registrationEmailTextInputLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun isValidPhoneNumber(): Boolean {
        val phoneNumber = binding.phoneNumberTextInputLayout.editText?.text.toString()
        val phoneRegex = "(\\(\\d{2}\\))(\\d{4,5}-\\d{4})"
        return when {
            phoneNumber.isEmpty() -> {
                binding.phoneNumberTextInputLayout.error =
                    getString(R.string.erro_field_cannot_be_empty)
                false
            }
            !phoneNumber.matches(phoneRegex.toRegex()) -> {
                binding.phoneNumberTextInputLayout.error =
                    getString(R.string.erro_invalid_phone_number)
                false
            }
            else -> {
                binding.phoneNumberTextInputLayout.error = null
                binding.phoneNumberTextInputLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun isValidCpf(): Boolean {
        val cpf: String = binding.cpfEditText.text.toString()
        return when {
            cpf.isEmpty() -> {
                binding.cpfTextInputLayout.error = getString(R.string.erro_field_cannot_be_empty)
                false
            }
            !CpfValidator.myValidateCPF(binding.cpfEditText.text.toString()) -> {
                binding.cpfTextInputLayout.error = getString(R.string.erro_invalid_cpf)
                false
            }
            else -> {
                binding.cpfTextInputLayout.error = null
                binding.cpfTextInputLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun isValidPassword(): Boolean {
        val senha = binding.registrationPasswordTextInputLayout.editText?.text.toString()
        val passwordVal = "^" + "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-zA-Z])" +  //any letter
                ".{3,8}" +  // between 3 and 8 digits
                "$"

        return when {
            senha.isEmpty() -> {
                binding.registrationPasswordTextInputLayout.error =
                    getString(R.string.erro_field_cannot_be_empty)
                false
            }
            senha.length < 3 -> {
                binding.registrationPasswordTextInputLayout.error =
                    getString(R.string.erro_password_too_short)
                false
            }
            senha.length > 8 -> {
                binding.registrationPasswordTextInputLayout.error =
                    getString(R.string.erro_password_too_long)
                false
            }
            !senha.matches(passwordVal.toRegex()) -> {
                binding.registrationPasswordTextInputLayout.error =
                    getString(R.string.erro_password_conditions)
                false
            }
            else -> {
                binding.registrationPasswordTextInputLayout.error = null
                binding.registrationPasswordTextInputLayout.isErrorEnabled = false
                true
            }
        }
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

    private fun saveData() {
        val firstName = binding.firstNameTextInputLayout.editText?.text.toString()
        val lastName = binding.lastNameTextInputLayout.editText?.text.toString()
        val email = binding.registrationEmailTextInputLayout.editText?.text.toString()
        val phoneNumber = binding.phoneNumberTextInputLayout.editText?.text.toString()
        val cpf = binding.cpfEditText.text.toString()

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
