package com.itigradteamsix.snapshop.authentication.signup.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var loadingDialog: AlertDialog



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
        loadingDialog= AlertDialog
            .Builder(requireContext())
            .setView(ProgressBar(requireContext()))
            .create()
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
            clearErrorMsgs()
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
            if (!password.isValidPassword()) {
                binding.passwordLayoutSignup.error = "Please enter valid password"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayoutSignup.error = "Please enter valid email"
                return@setOnClickListener
            }
            val signupUser = SignupUser(userNAme, email, password)

            viewModel.signUpUser(signupUser)
            Log.d("viewModelSignUpFun","done")



        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.signupResultFlow.collect { result ->
                when (result) {

                    is AuthState.Loading -> {
                        Log.d("signupFlowCollect", "Loading")
                        showLoadingDialog()
                    }

                    is AuthState.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Please verify your email and login",
                            Toast.LENGTH_SHORT
                        ).show()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_signupFragment_to_loginFragment)

                    }

                    is AuthState.Failure -> {
                        loadingDialog.dismiss()
                        Log.d("signupFlowFailure", result.exception.message.toString())
                        showMsgDialog("\n"+result.exception.message.toString())


                    }
                }

            }
        }


        }
    }
    private fun showMsgDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        val messageTextView = TextView(requireContext())

        messageTextView.text = message
        messageTextView.gravity = Gravity.CENTER

        val container = FrameLayout(requireContext())
        container.addView(messageTextView)

        builder.setView(container)
        builder.setCancelable(true)
        builder.setPositiveButton("Ok"){ _: DialogInterface?, _: Int ->
        }
        loadingDialog = builder.create()
        loadingDialog.apply {
            setIcon(R.drawable.baseline_info_24)
            setTitle("Warning!!")
        }
            .show()
    }
    private fun showLoadingDialog() {
        val progressBar = ProgressBar(requireContext())

        val container = FrameLayout(requireContext())
        container.addView(progressBar)

        loadingDialog = AlertDialog.Builder(requireContext())
            .setView(container)
            .create()
        loadingDialog.setCancelable(false)
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        loadingDialog.show()
    }

    fun String.isValidPassword(): Boolean {
        val passwordRegex =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return passwordRegex.matches(this)
    }
    private fun clearErrorMsgs() {
        binding.emailLayoutSignup.error = null
        binding.passwordLayoutSignup.error = null
        binding.usernameLayoutSignup.error = null


    }



}