package com.nk.blogapp.ui.activities.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nk.blogapp.databinding.BlogItemBinding
import com.nk.blogapp.model.BlogItemModel

class BlogItemAdapter : RecyclerView.Adapter<BlogItemAdapter.BlogItemAdapterViewModel>() {
    private val blogItemList = ArrayList<BlogItemModel>()

    inner class BlogItemAdapterViewModel(private val binding: BlogItemBinding) :
        ViewHolder(binding.root) {
        fun bind(item: BlogItemModel) {
            binding.blogTitle.text = item.title
            binding.blogShortDescription.text = item.post
            binding.bloggerName.text = item.userName
            binding.blogDate.text = item.date
            binding.likesCount.text = item.likeCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogItemAdapterViewModel {
        return BlogItemAdapterViewModel(
            BlogItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return blogItemList.size
    }

    override fun onBindViewHolder(holder: BlogItemAdapterViewModel, position: Int) {
        val blogItem = blogItemList[position]
        holder.bind(blogItem)
    }
}