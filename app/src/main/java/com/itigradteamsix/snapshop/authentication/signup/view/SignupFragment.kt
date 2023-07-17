package com.itigradteamsix.snapshop.authentication.signup.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.authentication.signup.viewModel.SignupViewModel
import com.itigradteamsix.snapshop.authentication.signup.viewModel.SignupViewModelFactory
import com.itigradteamsix.snapshop.databinding.FragmentSignupBinding
import kotlinx.coroutines.launch
import java.lang.Exception

class SignupFragment : Fragment() {
    private var email = ""
    private var password = ""
    private var userNAme = ""
    private lateinit var viewModel: SignupViewModel
    lateinit var binding: FragmentSignupBinding
    private val auth : FirebaseAuth =FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signupViewModelFactory = SignupViewModelFactory(FirebaseRepo(auth))
        viewModel = ViewModelProvider(requireActivity(),signupViewModelFactory).get(SignupViewModel::class.java)
        binding.txtLogin.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_signupFragment_to_loginFragment)
        }
        binding.asGuestSignup.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_signupFragment_to_homeFragment)
        }
        binding.signupBtn.setOnClickListener {
            email = binding.emailEditSignup.text.toString().trim()
            password = binding.passwordEditSignup.text.toString().trim()
            userNAme = binding.usernameEditSignup.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailLayoutSignup.error = "Please enter your email"
                return@setOnClickListener
            }
            if (userNAme.isEmpty()) {
                binding.usernameLayoutSignup.error = "Please enter your username"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordLayoutSignup.error = "Please enter your password"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.passwordLayoutSignup.error = "Please enter valid email"
                return@setOnClickListener
            }
            val signupUser = SignupUser(userNAme, email, password)

            viewModel.signUpUser(signupUser)
            Log.d("viewModelSignUpFun","done")


        }
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.signupResultFlow.collect { result  ->
                when (result) {

                    is AuthState.Loading -> {
                        Log.d("signupFlowCollect", "Loading")
                    }

                    is AuthState.Success -> {
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_signupFragment_to_homeFragment )
                    }

                    is AuthState.Failure -> {

                        Log.d("signupFlowFailure", result.exception.toString())

                    }
                }

            }


        }
    }




}