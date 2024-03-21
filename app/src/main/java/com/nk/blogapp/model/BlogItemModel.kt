package com.nk.blogapp.model

data class BlogItemModel(
    val title: String,
    val userName: String,
    val date: String,
    val post: String,
    val likeCount: Int
)