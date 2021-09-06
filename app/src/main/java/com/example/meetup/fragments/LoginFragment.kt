package com.example.meetup.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.meetup.R
import com.example.meetup.databinding.FragmentLoginBinding
import com.example.meetup.extensions.showToast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
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

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val callbackManager = CallbackManager.Factory.create()

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

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
        mAuth = Firebase.auth
        setupListeners()
        setupUi()
    }

    private fun setupUi() {
        binding.fbLoginButton.setPermissions("email", "public_profile")

        binding.fbLoginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
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

        //google
        if (requestCode == GOOGLE_SIGN_IN) {
            findNavController().navigate(R.id.action_loginFragment_to_recyclerViewFragment)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "onActivityResult Exception", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)//
        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGSO())

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "AuthGoogleFail", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getGSO(): GoogleSignInOptions {
        val clientId = "338665691016-ocoutm0dpm28mbkjhpv1v382c8beohql.apps.googleusercontent.com"
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
    }

    private fun setupListeners() {

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_emailLoginFragment)
        }
        binding.loginAnonymously.setOnClickListener {
            signInAnonymously()
        }
        binding.googleButton.setOnClickListener { signIn() }
    }

    private fun signInAnonymously() {
        mAuth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_loginFragment_to_recyclerViewFragment)
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
