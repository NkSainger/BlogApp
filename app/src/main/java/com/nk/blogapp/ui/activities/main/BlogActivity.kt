package com.nk.blogapp.ui.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nk.blogapp.databinding.ActivityBlogBinding
import com.nk.blogapp.viewmodel.BlogViewModel

class BlogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlogBinding
    private lateinit var blogViewModel: BlogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]


    }
}