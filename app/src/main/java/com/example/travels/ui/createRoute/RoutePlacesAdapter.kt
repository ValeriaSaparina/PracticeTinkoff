package com.example.travels.ui.createRoute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.R
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.PlacesDiffCallback
import com.example.travels.ui.places.model.PlaceUiModel

class RoutePlacesAdapter(
    private val onIcClicked: (PlaceUiModel) -> Unit,
    private val onItemLongClick: (PlaceUiModel) -> Boolean,
    private val isSelected: (String?) -> Boolean = { false },
) : ListAdapter<PlaceUiModel, RoutePlacesAdapter.ViewHolder>(PlacesDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoutePlacesAdapter.ViewHolder =
        ViewHolder(ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RoutePlacesAdapter.ViewHolder, position: Int) {
        holder.bind(item = getItem(position))
    }

    inner class ViewHolder(
        private val viewBinding: ItemPlaceBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: PlaceUiModel) {
            with(viewBinding) {
                nameTv.text = item.name
                descriptionTv.text = item.description
                favIc.setImageResource(R.drawable.outline_favorite_24)
                val color = if (isSelected(item.id)) {
                    R.color.md_theme_light_secondaryContainer
                } else {
                    R.color.md_thene_light_not_selected
                }
                content.setBackgroundResource(color)
                initListeners(item)
            }
        }

        private fun initListeners(item: PlaceUiModel) {
            with(viewBinding) {
                this.favIc.setOnClickListener {
                    onIcClicked(item)
                }
                cardView.setOnClickListener {
                    val color = if (!onItemLongClick(item)) {
                        R.color.md_thene_light_not_selected
                    } else {
                        R.color.md_theme_light_secondaryContainer
                    }
                    content.setBackgroundResource(color)
                }
            }
        }
    }
}