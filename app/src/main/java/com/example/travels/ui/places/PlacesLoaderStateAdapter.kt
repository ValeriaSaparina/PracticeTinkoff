package com.example.travels.ui.places

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.databinding.ItemErrorBinding
import com.example.travels.databinding.ItemProgressBinding

class PlacesLoaderStateAdapter(
    private val showError: (String) -> Unit
) : LoadStateAdapter<PlacesLoaderStateAdapter.ItemViewHolder>() {

    override fun getStateViewType(loadState: LoadState) = when (loadState) {
        is LoadState.NotLoading -> error("Not supported")
        is LoadState.Loading -> PROGRESS
        is LoadState.Error -> ERROR
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ItemViewHolder {
        return when (loadState) {
            is LoadState.Loading -> ProgressViewHolder(LayoutInflater.from(parent.context), parent)
            is LoadState.Error -> ErrorViewHolder(
                LayoutInflater.from(parent.context),
                parent,
                showError = showError
            )

            is LoadState.NotLoading -> error("Not supported")
        }
    }

    private companion object {
        private const val ERROR = 1
        private const val PROGRESS = 0
    }

    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(loadState: LoadState)
    }

    class ProgressViewHolder internal constructor(
        viewBinding: ItemProgressBinding
    ) : ItemViewHolder(viewBinding.root) {
        override fun bind(loadState: LoadState) {}

        companion object {
            operator fun invoke(
                layoutInflater: LayoutInflater,
                parent: ViewGroup? = null,
                attachToRoot: Boolean = false
            ): ProgressViewHolder {
                return ProgressViewHolder(
                    ItemProgressBinding.inflate(
                        layoutInflater,
                        parent,
                        attachToRoot
                    )
                )
            }
        }
    }

    class ErrorViewHolder internal constructor(
        private val viewBinding: ItemErrorBinding,
        private val showError: (String) -> Unit
    ) : ItemViewHolder(viewBinding.root) {

        override fun bind(loadState: LoadState) {
            require(loadState is LoadState.Error)
            showError(loadState.error.message.toString())
        }

        companion object {

            operator fun invoke(
                layoutInflater: LayoutInflater,
                parent: ViewGroup? = null,
                attachToRoot: Boolean = false,
                showError: (String) -> Unit
            ): ErrorViewHolder {
                return ErrorViewHolder(
                    viewBinding = ItemErrorBinding.inflate(
                        layoutInflater,
                        parent,
                        attachToRoot
                    ),
                    showError = showError
                )
            }
        }
    }

}