package com.nk.blogapp.model

data class UserModel(
    val name: String,
    val email: String,
    val imagePath: String = ""
) {
    constructor() : this("", "", "")
}