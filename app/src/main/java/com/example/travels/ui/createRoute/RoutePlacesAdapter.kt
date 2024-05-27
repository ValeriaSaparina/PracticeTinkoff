package com.example.travels.ui.createRoute

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.PlacesDiffCallback
import com.example.travels.ui.places.model.PlaceUiModel

class RoutePlacesAdapter(
    private val onIcClicked: (PlaceUiModel) -> Unit,
    private val onItemLongClick: (PlaceUiModel) -> Unit,
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
//                favIc.setBackgroundResource(R.drawable.baseline_delete_outline_24)
                initListeners(item)
            }
        }

        private fun initListeners(item: PlaceUiModel) {
            with(viewBinding) {
                favIc.setOnClickListener {
                    onIcClicked(item)
                }
                root.setOnLongClickListener {
                    onItemLongClick(item)
                    it.setBackgroundColor(Color.argb(128, 255, 255, 255))
                    true
                }
            }
        }
    }
}