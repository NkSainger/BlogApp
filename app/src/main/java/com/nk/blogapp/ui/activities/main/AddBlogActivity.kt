package com.nk.blogapp.ui.activities.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nk.blogapp.databinding.ActivityAddBlogBinding
import com.nk.blogapp.model.BlogItemModel
import com.nk.blogapp.viewmodel.BlogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddBlogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBlogBinding
    private lateinit var blogViewModel: BlogViewModel
    private var userName = ""

    init {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val firebaseFirestore = FirebaseFirestore.getInstance()

        try {
            CoroutineScope(Dispatchers.IO).launch {
                val usersCollection = firebaseFirestore.collection("data")
                val userDocument = uid?.let {
                    usersCollection.document(it).collection("user_data").document("user_details")
                }

                userDocument?.get()?.addOnSuccessListener { documentSnapshot ->
                    val name = documentSnapshot.getString("name")
                    if (name != null) {
                        userName = name // Assign the value only if it's not null
                        Log.d("TAG", "onCreate: $userName")
                    } else {
                        // Handle the case when "name" is null
                        Log.e("TAG", "Name is null")
                    }
                }?.addOnFailureListener { exception ->
                    // Handle the failure case
                    Log.e("TAG", "Error getting document", exception)
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this@AddBlogActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]

        binding.addBlogBtn.setOnClickListener {
            binding.apply {
                val blogTitle = edBlogTitle.text.toString().trim()
                val blogDescription = edBlogDescription.text.toString().trim()

                if (blogTitle == "") {
                    edBlogTitle.error = "Please Enter the Blog Title"
                } else if (blogDescription == "") {
                    edBlogDescription.error = "Please Enter the Blog Description"
                } else {
                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    if (userName != "") {
                        val blogItem = BlogItemModel(
                            blogTitle, userName, currentDate.format(formatter), blogDescription, 0
                        )
                        blogViewModel.addBlog(this@AddBlogActivity, blogItem)
                    }
                }
            }
        }
    }
}