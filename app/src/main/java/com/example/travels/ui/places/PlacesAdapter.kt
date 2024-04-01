package com.example.travels.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.model.ItemUiModel

class PlacesAdapter(
    private val items: List<ItemUiModel?>,
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position]?.let { holder.bind(it) }
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
}