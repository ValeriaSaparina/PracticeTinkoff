package com.example.travels.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.R
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.model.PlaceUiModel

class PlacesAdapter(
    private val onItemClicked: (PlaceUiModel) -> Unit,
    private val onFavIcClicked: (PlaceUiModel) -> Unit
) :
    PagingDataAdapter<PlaceUiModel, PlacesAdapter.ViewHolder>(PlacesDiffCallback()) {

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
        fun bind(item: PlaceUiModel) {
            with(viewBinding) {
                nameTv.text = item.name
                descriptionTv.text = item.description
                setIcon(item.isFav)
                favIc.setOnClickListener {
                    onFavIcClicked(item)
                    setIcon(!item.isFav)
                }
                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }

        private fun setIcon(isFav: Boolean) {
            val ic =
                if (isFav) R.drawable.outline_favorite_24 else R.drawable.outline_favorite_border_24
            viewBinding.favIc.setBackgroundResource(ic)
        }
    }


    private class PlacesDiffCallback : DiffUtil.ItemCallback<PlaceUiModel>() {
        override fun areItemsTheSame(oldItem: PlaceUiModel, newItem: PlaceUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlaceUiModel, newItem: PlaceUiModel): Boolean {
            return oldItem == newItem
        }

    }
}