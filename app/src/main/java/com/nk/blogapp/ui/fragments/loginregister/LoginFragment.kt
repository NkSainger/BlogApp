package com.nk.blogapp.ui.fragments.loginregister

import android.content.Intent
import android.os.Bundle
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
import com.nk.blogapp.databinding.FragmentLoginBinding
import com.nk.blogapp.ui.activities.main.MainActivity
import com.nk.blogapp.util.CoroutineDispatcherProvider.mainDispatcher
import com.nk.blogapp.util.RegisterValidation
import com.nk.blogapp.util.Resource
import com.nk.blogapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Override the back button behavior
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to the destination that clears the back stack
                findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {
                            //
                        }

                        is Resource.Success -> {
                            Toast.makeText(activity, "Successfully Logged In", Toast.LENGTH_SHORT)
                                .show()
                            Intent(requireActivity(), MainActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT)
                                .show()
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