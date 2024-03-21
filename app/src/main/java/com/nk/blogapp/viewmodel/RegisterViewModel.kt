package com.nk.blogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nk.blogapp.model.UserModel
import com.nk.blogapp.util.Constants.USER_COLLECTION
import com.nk.blogapp.util.RegisterFieldsState
import com.nk.blogapp.util.RegisterValidation
import com.nk.blogapp.util.Resource
import com.nk.blogapp.util.validateEMail
import com.nk.blogapp.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<UserModel>>(Resource.Unspecified())
    val register: Flow<Resource<UserModel>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()
    fun createAccountWithEmailAndPassword(
        userModel: UserModel,
        password: String
    ) {
        if (checkValidation(userModel, password)) {
            runBlocking {
                _register.value = Resource.Loading()
            }
            firebaseAuth.createUserWithEmailAndPassword(userModel.email, password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.let {
                        saveUserInfo(it.uid, userModel)
                    }
                }

                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEMail(userModel.email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun saveUserInfo(userUid: String, userModel: UserModel) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .collection("user_data")
            .document("user_details")
            .set(userModel)
            .addOnSuccessListener {
                _register.value = Resource.Success(userModel)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(userModel: UserModel, password: String): Boolean {
        val emailValidation = validateEMail(userModel.email)
        val passwordValidation = validatePassword(password)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }
}