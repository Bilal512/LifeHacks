package com.simbaone.lifehacks.presentation.carousal

import android.graphics.Paint
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simbaone.lifehacks.constants.Constants
import com.simbaone.lifehacks.databinding.ItemCarousalBinding
import com.simbaone.lifehacks.databinding.ItemNativeAdBinding
import com.simbaone.lifehacks.domain.model.LifeHack
import com.simbaone.lifehacks.domain.repo.LogRepo
import javax.inject.Inject

const val NATIVE_AD_VIEW = 1
const val ITEM_VIEW = 2

class CarousalAdapter : ListAdapter<LifeHack, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_VIEW) {
            ItemCarousalBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
                return ViewHolder(it)
            }
        } else {
            ItemNativeAdBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
                return NativeAdViewHolder(it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).data == Constants.NATIVE_AD_UUID) {
            NATIVE_AD_VIEW
        } else {
            ITEM_VIEW
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(getItem(position))
            }

            is NativeAdViewHolder -> {

            }
        }
    }

    inner class ViewHolder(private val binding: ItemCarousalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lifeHack: LifeHack) {
            binding.apply {
                tvHeading.paintFlags = tvHeading.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                tvData.text =
                    Html.fromHtml(lifeHack.spanText.toString(), Html.FROM_HTML_MODE_LEGACY)
                tvData.movementMethod = LinkMovementMethod.getInstance()

                tvPageNumber.text = (layoutPosition + 1).toString()
            }
        }

    }

    inner class NativeAdViewHolder(private val binding: ItemNativeAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    class DiffCallback : DiffUtil.ItemCallback<LifeHack>() {
        override fun areItemsTheSame(oldItem: LifeHack, newItem: LifeHack): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LifeHack, newItem: LifeHack): Boolean {
            return oldItem == newItem
        }

    }
}