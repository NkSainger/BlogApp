package com.nk.blogapp.utils

import android.util.Patterns

fun validateEMail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email can't be empty")

//    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password can't be empty")

    if (password.length < 6)
        return RegisterValidation.Failed("Password must contain 6 or more characters")

    return RegisterValidation.Success
}