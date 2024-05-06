package com.example.travels.ui.places

import androidx.recyclerview.widget.RecyclerView
import com.example.travels.R
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.model.PlaceUiModel

open class PlacesViewHolder(
    private val onItemClicked: (PlaceUiModel) -> Unit,
    private val onFavIcClicked: (PlaceUiModel) -> Unit,
    private val viewBinding: ItemPlaceBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {
    open fun bind(item: PlaceUiModel) {
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
