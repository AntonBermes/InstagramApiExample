package com.antonbermes.instagramapiexample.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.antonbermes.instagramapiexample.databinding.ListItemBinding
import com.antonbermes.instagramapiexample.domain.Image

class MyListAdapter(private val clickListener: ListListener) : PagedListAdapter<Image,
        MyListAdapter.ViewHolder>(ListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ListListener, item: Image?) {
            binding.image = item
            binding.cardViewClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ListDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}

class ListListener(val clickListenerCard: (image: Image) -> Unit) {
    fun onClickCard(image: Image) = clickListenerCard(image)
}