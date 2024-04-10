package com.example.travels.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.model.ItemUiModel

class PlacesAdapter(
) : ListAdapter<ItemUiModel?, PlacesAdapter.ViewHolder>(PlacesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val viewBinding: ItemPlaceBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: ItemUiModel) {
            with(viewBinding) {
                nameTv.text = item.name
                descriptionTv.text = item.description
            }
        }

    }

    private class PlacesDiffCallback : DiffUtil.ItemCallback<ItemUiModel?>() {
        override fun areItemsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            return oldItem == newItem // TODO: diffCallback
        }

        override fun areContentsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            return oldItem == newItem // TODO: diffCallback
        }

    }
}