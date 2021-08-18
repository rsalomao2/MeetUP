package com.example.fixingfirebase.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.meetup.R
import com.example.meetup.databinding.FragmentLoginBinding
import com.example.meetup.extensions.showToast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {
	//    private lateinit var callbackManager: CallbackManager

	private val callbackManager = CallbackManager.Factory.create()

	private lateinit var binding: FragmentLoginBinding
	private lateinit var mAuth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient

	companion object {
		const val GOOGLE_SIGN_IN = 1903
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupListeners()
		mAuth = Firebase.auth
//        facebookLogin()
		setupListeners()
		setupUi()
	}

	private fun setupUi() {
		binding.fbLoginButton.setPermissions("email", "public_profile")

		binding.fbLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
			override fun onCancel() = Unit
			override fun onSuccess(loginResult: LoginResult) {
				Log.d("###", "facebook:onSuccess:$loginResult")
				handleFacebookAccessToken(loginResult.accessToken)
			}

			override fun onError(error: FacebookException) {
				Log.d("###", "facebook:onError", error)
			}
		})
	}

	override fun onStart() {
		super.onStart()
		val currentUser = mAuth.currentUser
		if (currentUser != null) {
			requireContext().showToast("FUNCIONA krai temos um user")
		} else
			requireContext().showToast("FUNCIONA krai NAO temos um user")
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

		// Pass the activity result back to the Facebook SDK
		callbackManager.onActivityResult(requestCode, resultCode, data)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
//        callbackManager = CallbackManager.Factory.create()
//        mAuth = Firebase.auth
//        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGSO())
//        binding.googleButton.setOnClickListener { signIn() }
	}

//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
//    }

//	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//		super.onActivityResult(requestCode, resultCode, data)
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GOOGLE_SIGN_IN) {
//            findNavController().navigate(R.id.action_loginFragment_to_recyclerViewFragment)
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Toast.makeText(context, "onActivityResult Exception", Toast.LENGTH_SHORT).show()
//            }
//	}
//}

//    private fun facebookLogin(){
//        val btnLoginFacebook = binding.fbLoginButton
//
//        btnLoginFacebook.setOnClickListener {
//            // Login
//            callbackManager = CallbackManager.Factory.create()
//            LoginManager.getInstance()
//                .logInWithReadPermissions(this, listOf("public_profile", "email"))
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(loginResult: LoginResult) {
//                        findNavController().navigate(R.id.action_loginFragment_to_recyclerViewFragment)
//                        Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
//                    }
//
//                    override fun onCancel() {
//                        Log.d("MainActivity", "Facebook onCancel.")
//                    }
//
//                    override fun onError(error: FacebookException) {
//                        Log.d("MainActivity", "Facebook onError.")
//                    }
//                })
//        }
//    }

//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        mAuth.signInWithCredential(credential)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(context, "AuthGoogleFail", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    private fun getGSO(): GoogleSignInOptions {
//        val clientId = "338665691016-ocoutm0dpm28mbkjhpv1v382c8beohql.apps.googleusercontent.com"
//        return  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(clientId)
//            .requestEmail()
//            .build()
//    }

	private fun setupListeners() {
		binding.emailEditText.addTextChangedListener(TextFieldValidation(binding.emailEditText))
		binding.passwordEditText.addTextChangedListener(TextFieldValidation(binding.passwordEditText))
		binding.loginButton.setOnClickListener {
			signInAnonymously()
		}
//		binding.signUpTextView.setOnClickListener {
//			findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
//		}
//	binding.loginButton.setOnClickListener {
//		if (isValidEmail() and isValidPassword() and emailErrorDialog()) {
//			val email: String = binding.textInputLayoutEmail.editText?.text.toString()
//			val action =
//				LoginFragmentDirections.actionLoginFragmentToHomeFragment(email)
//			findNavController().navigate(action)
//		}
//	}
	}

	private fun isValidPassword(): Boolean {
		val password: String = binding.senhaTextInputLayout.editText?.text.toString()
		val passwordValidationRegex = "^" + "(?=.*[0-9])" + //at least 1 digit
				"(?=.*[a-zA-Z])" + //any letter
				".{3,8}" +  // between 3 and 8 digits
				"$"

		return when {
			password.isEmpty() -> {
				binding.senhaTextInputLayout.error = getString(R.string.erro_field_cannot_be_empty)
				false
			}
			password.length < 3 -> {
				binding.senhaTextInputLayout.error = getString(R.string.erro_password_too_short)
				false
			}
			password.length > 8 -> {
				binding.senhaTextInputLayout.error = getString(R.string.erro_password_too_long)
				false
			}
			!password.matches(passwordValidationRegex.toRegex()) -> {
				binding.senhaTextInputLayout.error = getString(R.string.erro_password_conditions)
				false
			}
			else -> {
				binding.senhaTextInputLayout.error = null
				binding.senhaTextInputLayout.isErrorEnabled = false
				true
			}
		}
	}

	private fun isValidEmail(): Boolean {

		val email: String = binding.textInputLayoutEmail.editText?.text.toString()
		val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

		return when {
			email.isEmpty() -> {
				binding.textInputLayoutEmail.error = getString(R.string.erro_field_cannot_be_empty)
				false
			}
			!email.matches(emailPattern.toRegex()) -> {
				binding.textInputLayoutEmail.error = getString(R.string.erro_invalid_email_adress)
				false
			}
			else -> {
				binding.textInputLayoutEmail.error = null
				binding.textInputLayoutEmail.isErrorEnabled = false
				true
			}
		}
	}

	inner class TextFieldValidation(private val view: View) : TextWatcher {
		override fun afterTextChanged(s: Editable?) {}
		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			when (view.id) {
				R.id.emailEditText -> isValidEmail()
				R.id.passwordEditText -> isValidPassword()
			}
		}
	}

//	private fun emailErrorDialog(): Boolean {
//		val email: String = binding.textInputLayoutEmail.editText?.text.toString()
//		val sharedPreferences =
//			requireActivity().getSharedPreferences(email, Context.MODE_PRIVATE)
//		val savedEmail =
//			sharedPreferences.getString("EMAIL", "")
//
//		return when {
//			email != savedEmail -> {
//				val emailErrorDialog = AlertDialog.Builder(context)
//					.setTitle("Unregistred Email!")
//					.setMessage("Create an account ?")
//					.setIcon(R.drawable.ic_error)
//					.setPositiveButton("Yes") { _, _ ->
//						findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
//					}
//					.setNegativeButton("No") { _, _ ->
//					}.create()
//				emailErrorDialog.show()
//				false
//			}
//			else -> {
//				true
//			}
//		}
//	}

	private fun signInAnonymously() {
		mAuth.signInAnonymously()
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					requireContext().showToast("Sucesso: ${mAuth.currentUser}")
				} else {
					requireContext().showToast("Error: Krai nao deu")
					Log.d("###", task.exception?.localizedMessage ?: "Error ao login anonnymously")
				}
			}
	}

	private fun handleFacebookAccessToken(token: AccessToken) {
		Log.d("###", "handleFacebookAccessToken:$token")

		val credential = FacebookAuthProvider.getCredential(token.token)
		mAuth.signInWithCredential(credential)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d("###", "signInWithCredential:success")
					val user = mAuth.currentUser
					requireContext().showToast("LOGADO: ${user?.displayName}")
				} else {
					// If sign in fails, display a message to the user.
					requireContext().showToast("Authentication failed.: task.exception")
					Log.d("###", "signInWithCredential:failure", task.exception)
				}
			}
	}

}
