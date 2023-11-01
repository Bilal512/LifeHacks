package com.simbaone.lifehacks.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simbaone.lifehacks.data.models.CategoryDTO
import com.simbaone.lifehacks.databinding.ItemCategoryBinding
import com.simbaone.lifehacks.extensions.getDrawableByName
import com.simbaone.lifehacks.utils.ColorUtils

class CategoriesAdapter(
    private val onItemClick: (category: CategoryDTO) -> Unit
) : ListAdapter<CategoryDTO, CategoriesAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.ViewHolder {
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
            return ViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryDTO) {
            binding.apply {
                tvName.text = category.name
                tvCount.text = category.count.toString()
                root.setOnClickListener { onItemClick(category) }
                rootCardView.setCardBackgroundColor(category.bgColor!!)

                root.context.getDrawableByName(category.iconName)?.let {
                    ivIcon.setImageDrawable(it)
                }

                ColorUtils.getContrastColor(category.bgColor).also {
                    tvName.setTextColor(it)
                    tvCount.setTextColor(it)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryDTO>() {
        override fun areItemsTheSame(oldItem: CategoryDTO, newItem: CategoryDTO): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoryDTO, newItem: CategoryDTO): Boolean {
            return oldItem == newItem
        }

    }
}
