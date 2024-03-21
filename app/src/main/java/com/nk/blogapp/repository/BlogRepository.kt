package com.nk.blogapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nk.blogapp.model.BlogItemModel
import java.util.UUID
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
){
    suspend fun addBlog(blogItemModel: BlogItemModel, blogId: UUID) {
        firebaseAuth.currentUser?.uid?.let {
            db.collection("Blogs")
                .document(it)
                .collection(blogId.toString())
                .document("BlogDetails")
                .set(blogItemModel)
        }
    }
}