package com.example.meetup.presentation.signin.social

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.meetup.R
import com.example.meetup.databinding.FragmentLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment(R.layout.fragment_login) {

    private val callbackManager = CallbackManager.Factory.create()

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: SignInViewModel

    companion object {
        const val GOOGLE_SIGN_IN = 1903
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        val application = requireNotNull(this.activity).application
        mAuth = Firebase.auth
        val fbLoginManager = LoginManager.getInstance()
        viewModel =
            ViewModelProviders.of(this, SignInViewModelFactory(mAuth, fbLoginManager, application))
                .get(SignInViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        setupAnonimouslySingInObserver()
        setupFirebaseLoginTaskObserver()
    }

    private fun setupFirebaseLoginTaskObserver() {
        viewModel.firebaseLoginTask.observe(viewLifecycleOwner) { task ->
            if (task) {
                navigateToHome()
            }
        }
    }

    private fun setupAnonimouslySingInObserver() {
        viewModel.anonimouslyTask.observe(viewLifecycleOwner) { task ->
            if (task) {
                navigateToHome()
            }
        }
    }

    private fun setupLoginFacebook() {
        val btnLoginFacebook = binding.fbLoginButton
        val btnLoginFacebookFake = binding.fbLoginButtonFake
        btnLoginFacebookFake.setOnClickListener {
            btnLoginFacebook.callOnClick()
        }
        btnLoginFacebook.setOnClickListener {
            val instance = LoginManager.getInstance()
            instance
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val token = loginResult.accessToken.token
                        val credential = FacebookAuthProvider.getCredential(token)
                        viewModel.signInOnFirebase(credential)
                        Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
                    }

                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d("MainActivity", "Facebook onError.")
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                viewModel.signInOnFirebase(credential)
            } catch (e: ApiException) {
                Toast.makeText(context, "onActivityResult Exception", Toast.LENGTH_SHORT).show()
                Log.d("###", e.message.toString())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)//
        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGSO())

    }

    private fun showGoogleSignInDialog() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun getGSO(): GoogleSignInOptions {
        val clientId = "169900779180-t22ieegpib8g65i7cu00sk52sfmga9ui.apps.googleusercontent.com"
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
    }

    private fun setupListeners() {
        setupLoginFacebook()
        setupLoginEmail()
        setupLoginAnounymously()
        setupLoginGoogleButton()
        setupSignUoButton()
    }

    private fun setupSignUoButton() {
        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }
    }

    private fun setupLoginAnounymously() {
        binding.loginAnonymously.setOnClickListener {
            viewModel.signInAnonymously()
        }
    }

    private fun setupLoginEmail() {
        binding.loginEmailBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_emailLoginFragment)
        }
    }

    private fun setupLoginGoogleButton() {
        binding.googleButtonFake.setOnClickListener {
            showGoogleSignInDialog()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_recyclerViewFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d("###", "FUNCIONA KRO MENU")
        menu.findItem(R.id.menu_logout).setVisible(false)
        Log.d("###", "FUNCIONA KRO MENU")
    }
}
