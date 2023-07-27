package com.itigradteamsix.snapshop.authentication.login.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.itigradteamsix.snapshop.MainActivity
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.authentication.login.model.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModel
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModelFactory
import com.itigradteamsix.snapshop.authentication.signup.model.APiDraftState
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.signup.viewModel.SignupViewModel
import com.itigradteamsix.snapshop.authentication.signup.viewModel.SignupViewModelFactory
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.databinding.FragmentLoginBinding
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.LineItems
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var email = ""
    private var password = ""
    private lateinit var viewModel: LoginViewModel
    private lateinit var signupViewModel: SignupViewModel
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var loadingDialog: AlertDialog
    private lateinit var failureDialog: AlertDialog
    private var customer: Customer? = null
    private val RC_SIGN_IN = 123
    private lateinit var googleSignInClient: GoogleSignInClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("816262035436-t2v98he45fijhlkbgqsm3jhq535atsrk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        loadingDialog = AlertDialog
            .Builder(requireContext())
            .setView(ProgressBar(requireContext()))
            .create()
        failureDialog = AlertDialog
            .Builder(requireContext())
            .create()
        val loginViewModelFactory = LoginViewModelFactory(FirebaseRepo(auth))
        viewModel = ViewModelProvider(
            requireActivity(),
            loginViewModelFactory
        ).get(LoginViewModel::class.java)
        val signupViewModelFactory = SignupViewModelFactory(FirebaseRepo(auth))
         signupViewModel = ViewModelProvider(
            requireActivity(),
            signupViewModelFactory
        ).get(SignupViewModel::class.java)
        binding.txtSignUp.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFragment2_to_signupFragment2)
        }

        binding.asGuest.setOnClickListener {
            viewModel.addUserToDataStore(isGuest = true,null)
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()

        }
        binding.google.setOnClickListener {
            signInWithGmail()

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
            viewModel.loginUser(email, password)
            Log.d("viewModelLoginFun", "done")
            viewLifecycleOwner.lifecycleScope.launch {

                viewModel.loginResultFlow.collect { result ->
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

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Your email isn't verified",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is AuthState.Failure -> {
                            loadingDialog.dismiss()
                            Log.d("signupFlowFailure", result.exception.message.toString())
                            showMsgDialog("\n" + result.exception.message.toString())
                        }
                    }

                }


            }
        }
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.customerByEmailFlow.collect { result ->
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
                        if(result.customerData.isNullOrEmpty())
                            {
                                Toast.makeText(requireContext(),"Sorry you have to sign up first",Toast.LENGTH_SHORT).show()
                                signupViewModel.createDraftOrder(DraftOrderResponse(DraftOrder(line_items = listOf( LineItems(
                                    title = "title",
                                    quantity = 1,
                                    price = "0"
                                ))
                                )))
                            }
                        else{
                            Log.d("draftID", "in success")
                            Log.d("draftID", result.customerData?.get(0)?.note.toString())
                            customer = result.customerData?.get(0)
                            viewModel.getDraftOrder(result.customerData?.get(0)?.note.toString())

                        //add user to datastore to avoid login again
                        customer?.let {
                            viewModel.addUserToDataStore(false, it)

                        }


                    }


                }
                    is ApiCustomerLoginState.Failure -> {
                        Log.d("loginApiFlowCollect", result.exception.message.toString())
                        showMsgDialog("\n" + result.exception.message.toString())
                    }

                    else -> {}
                }


        }}
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getDraftFlow.collect { result ->
                when (result) {

                    is ApiDraftLoginState.Loading -> {
                        Log.d("loginApiFlowCollect", "Loading")

                    }

                    is ApiDraftLoginState.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Welcome " + customer?.first_name,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("draftDataInSuccess", result.data?.id.toString())
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("customerID", customer?.id.toString())
                        intent.putExtra("draftID", result.data?.id.toString())
                        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("draftID",result.data?.id.toString())
                        editor.apply()
                        sharedPreferences.getString("draftID","")?.let { Log.d("draftIDLogin", it) }
                        startActivity(intent)
                        activity?.finish()
//                        Log.d("draftID", result.customerData?.get(0)?.note.toString())
//
//                        viewModel.getDraftOrder(result.customerData?.get(0)?.note.toString())


                    }

                    is ApiDraftLoginState.Failure -> {
                        Log.d("loginApiFlowCollect", result.exception.message.toString())
                        showMsgDialog("\n" + result.exception.message.toString())
                    }
                }

            }


        }

        viewLifecycleOwner.lifecycleScope.launch {

            signupViewModel.createDraftFlow.collect { result ->
                when (result) {

                    is APiDraftState.Loading -> {
                        Log.d("createCustomerFlowCollect", "Loading")

                    }

                    is APiDraftState.Success -> {
//                        Log.d("customerDataInSuccessSignup",result.customerData.toString())
//                        customerId = result.customerData?.id.toString()
                        signupViewModel.createCustomer(CustomerResponse( Customer(email = email
                            , first_name = auth.currentUser?.displayName
                            , note = result.data?.id.toString())
                        ))

//                        viewModel.createDraftOrder(DraftOrderResponse(DraftOrder(customer = result.customerData)))
                        Log.d("customerDataInSuccessSignup",result.data.toString())
                    }

                    is APiDraftState.Failure -> {
//                        loadingDialog.dismiss()
                        Log.d("createDraftFlowFailure", result.exception.message.toString())


                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {

            signupViewModel.createCustomerResultFlow.collect { result ->
                when (result) {

                    is ApiCustomerState.Loading -> {
                        Log.d("createCustomerFlowCollect", "Loading")

                    }

                    is ApiCustomerState.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Welcome " + customer?.first_name,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("draftDataInSuccess", result.customerData?.id.toString())
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("customerID", customer?.id.toString())
                        intent.putExtra("draftID", result.customerData?.id.toString())
                        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("draftID",result.customerData?.id.toString())
                        editor.apply()
                        sharedPreferences.getString("draftID","")?.let { Log.d("draftIDLogin", it) }
                        startActivity(intent)
                        activity?.finish()
                    }

                    is ApiCustomerState.Failure -> {
                        loadingDialog.dismiss()
                        Log.d("createCustomerFlowFailure", result.exception.message.toString())
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
        builder.setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
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
     fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign-in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    user?.email
                    Log.d("email gmail",user?.email.toString())
                    email=user?.email.toString()
                    //here is the problem
                    viewModel.getCustomerByEmail(
                        user?.email!!
                    )
                    // You can handle the signed-in user here
                } else {
                    Log.d("error","error at the firebase auth with google")
                }
            }
    }
    private fun signInWithGmail() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, handle error here
            }
        }
    }

}