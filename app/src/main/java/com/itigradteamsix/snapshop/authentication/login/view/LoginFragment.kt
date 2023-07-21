package com.itigradteamsix.snapshop.authentication.login.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.itigradteamsix.snapshop.MainActivity
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.authentication.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModel
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModelFactory
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private var email = ""
    private var password = ""
    private lateinit var viewModel: LoginViewModel
    private val auth : FirebaseAuth =FirebaseAuth.getInstance()
    private  lateinit var loadingDialog: AlertDialog
    private  lateinit var failureDialog: AlertDialog
    private var customer : Customer? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater , container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = AlertDialog
                .Builder(requireContext())
            .setView(ProgressBar(requireContext()))
            .create()
        failureDialog = AlertDialog
            .Builder(requireContext())
            .create()
        val loginViewModelFactory = LoginViewModelFactory(FirebaseRepo(auth))
        viewModel = ViewModelProvider(requireActivity(),loginViewModelFactory).get(LoginViewModel::class.java)
        binding.txtSignUp.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFragment2_to_signupFragment2)
        }

        binding.asGuest.setOnClickListener {

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.loginBtn.setOnClickListener {

            email = binding.emailEditLogin.text.toString().trim()
            password = binding.passwordEditLogin.text.toString().trim()
            clearErrorMsgs()
            if (email.isEmpty()) {
                binding.emailLayoutLogin.error = "Please enter your email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordLayoutLogin.error = "Please enter your username"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayoutLogin.error = "Please enter valid email"
                return@setOnClickListener
            }
            viewModel.loginUser(email,password)
            Log.d("viewModelLoginFun","done")
            viewLifecycleOwner.lifecycleScope.launch {

                viewModel.loginResultFlow.collect { result  ->
                    when (result) {

                        is AuthState.Loading -> {
                            Log.d("signupFlowCollect", "Loading")
                            //show loading alert here
                            showLoadingDialog()
                        }

                        is AuthState.Success -> {
                            loadingDialog.dismiss()
                            if (result.data) {

//                                val intent = Intent(activity, MainActivity::class.java)
//                                startActivity(intent)
//                                activity?.finish()
                                Log.d("emailBeforeGetBYEmail", email)
                                viewModel.getCustomerByEmail(email)
                                Log.d("emailafterGetBYEmail", email)

                            }
                            else{
                                Toast.makeText(requireContext(),"Your email isn't verified",Toast.LENGTH_SHORT).show()
                            }
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
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.customerByEmailFlow.collect { result  ->
                when (result) {

                    is ApiCustomerLoginState.Loading -> {
                        Log.d("loginApiFlowCollect", "Loading")

                    }

                    is ApiCustomerLoginState.Success -> {
//                        Toast.makeText(requireContext(),"Welcome "+result.customerData?.get(0)?.first_name,Toast.LENGTH_SHORT).show()
//                        Log.d("customerDataInSuccess",result.customerData.toString())
//                        val intent = Intent(activity, MainActivity::class.java)
//                        intent.putExtra("ID",result.customerData?.get(0)?.id)
//                        startActivity(intent)
//                        activity?.finish()
                        Log.d("draftID", "in success")
                        Log.d("draftID", result.customerData?.get(0)?.note.toString())
                        customer = result.customerData?.get(0)
                        viewModel.getDraftOrder(result.customerData?.get(0)?.note.toString())


                    }

                    is ApiCustomerLoginState.Failure -> {
                        Log.d("loginApiFlowCollect", result.exception.message.toString())
                        showMsgDialog("\n"+result.exception.message.toString())
                    }
                }

            }


        }
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getDraftFlow.collect { result  ->
                when (result) {

                    is ApiDraftLoginState.Loading -> {
                        Log.d("loginApiFlowCollect", "Loading")

                    }

                    is ApiDraftLoginState.Success -> {
                        Toast.makeText(requireContext(),"Welcome "+customer?.first_name,Toast.LENGTH_SHORT).show()
                        Log.d("draftDataInSuccess",result.data?.id.toString())
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("customerID",customer?.id.toString())
                        intent.putExtra("draftID",result.data?.id.toString())

                        startActivity(intent)
                        activity?.finish()
//                        Log.d("draftID", result.customerData?.get(0)?.note.toString())
//
//                        viewModel.getDraftOrder(result.customerData?.get(0)?.note.toString())


                    }

                    is ApiDraftLoginState.Failure -> {
                        Log.d("loginApiFlowCollect", result.exception.message.toString())
                        showMsgDialog("\n"+result.exception.message.toString())
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
        failureDialog = builder.create()
        failureDialog.apply {
            setIcon(R.drawable.baseline_info_24)
            setTitle("warning")
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
    private fun clearErrorMsgs() {
        binding.emailLayoutLogin.error = null
        binding.passwordLayoutLogin.error = null


    }

}