package xyz.codegeek.outbreakvisualizer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.codegeek.outbreakvisualizer.data.database.RegionalData
import xyz.codegeek.outbreakvisualizer.data.database.RegionalMapFeature
import xyz.codegeek.outbreakvisualizer.databinding.ListItemCountryDataBinding

class TrendsAdapter(): ListAdapter<RegionalData, TrendsAdapter.TrendsViewHolder>(TrendsDiffCallback()) {

    override fun onBindViewHolder(holder: TrendsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendsViewHolder {
        return TrendsViewHolder.from(parent)
    }
    class TrendsViewHolder private constructor(val binding: ListItemCountryDataBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: RegionalData){
            binding.regionalData = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): TrendsViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCountryDataBinding.inflate(layoutInflater, parent, false)
                return TrendsViewHolder(binding)
            }
        }
    }
}

class TrendsDiffCallback : DiffUtil.ItemCallback<RegionalData>(){
    override fun areContentsTheSame(oldItem: RegionalData, newItem: RegionalData): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: RegionalData, newItem: RegionalData): Boolean {
        return oldItem.id == newItem.id
    }
}