package com.nk.blogapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nk.blogapp.model.BlogItemModel
import com.nk.blogapp.repository.BlogRepository
import com.nk.blogapp.util.CoroutineDispatcherProvider.ioDispatcher
import com.nk.blogapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {
    private val _blog = MutableStateFlow<Resource<BlogItemModel>>(Resource.Unspecified())
    val blog: Flow<Resource<BlogItemModel>> = _blog

    private val blogRepository = BlogRepository(firebaseAuth, db)

    fun addBlog(context: Context, blogItemModel: BlogItemModel) =
        viewModelScope.launch(ioDispatcher) {
            val blogId = UUID.randomUUID()

            try {
                blogRepository.addBlog(blogItemModel, blogId)
                _blog.value
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
}