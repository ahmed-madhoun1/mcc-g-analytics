package com.example.analyticsassignment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.analyticsassignment.databinding.ProductItemBinding

class ProductAdapter(val context: Context, val listener: OnItemClickListener) :
    ListAdapter<Product, ProductAdapter.TasksViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class TasksViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // This will be executed when viewHolder is initiated
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = getItem(position)
                        listener.onItemClick(product)
                    }
                }
            }
        }


        fun bind(product: Product) {
            binding.apply {
                nameProduct.text = product.name
                detailsProduct.text = product.details
                Glide.with(context).load(product.image).into(imageViewProduct)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }

}