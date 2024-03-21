package com.nk.blogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nk.blogapp.util.RegisterFieldsState
import com.nk.blogapp.util.RegisterValidation
import com.nk.blogapp.util.Resource
import com.nk.blogapp.util.validateEMail
import com.nk.blogapp.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun login(email: String, password: String) {
        if (checkValidation(email, password)) {
            viewModelScope.launch {
                _login.emit(Resource.Loading())
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEMail(email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(email: String, password: String): Boolean {
        val emailValidation = validateEMail(email)
        val passwordValidation = validatePassword(password)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }
}