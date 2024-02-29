package com.nk.blogapp.model

data class User(
    val name: String,
    val email: String,
    val password: String,
    val imagePath: String = ""
) {
    constructor() : this("", "", "", "")
}