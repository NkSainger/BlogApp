package com.nk.blogapp.ui.fragments.loginregister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nk.blogapp.R
import com.nk.blogapp.databinding.FragmentRegisterBinding
import com.nk.blogapp.model.UserModel
import com.nk.blogapp.util.CoroutineDispatcherProvider.mainDispatcher
import com.nk.blogapp.util.RegisterValidation
import com.nk.blogapp.util.Resource
import com.nk.blogapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        // Override the back button behavior
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to the destination that clears the back stack
                findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            registerButton.setOnClickListener {
                val userModel = UserModel(
                    edName.text.toString().trim(),
                    edEmail.text.toString().trim()
                )

                Log.d("EMAIL", userModel.email)
                val password = edPassword.text.toString().trim()

                viewModel.createAccountWithEmailAndPassword(
                    userModel,
                    password
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect {
                    when (it) {
                        is Resource.Loading -> {
                            //
                        }

                        is Resource.Success -> {
                            Toast.makeText(activity, "Registered Successfully", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                activity,
                                "Got an Failure ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(mainDispatcher) {
                            binding.edEmail.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }

                    if (validation.password is RegisterValidation.Failed) {
                        withContext(mainDispatcher) {
                            binding.edPassword.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                }
            }
        }
    }
}