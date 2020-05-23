package com.e.speedy.runTracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.e.speedy.database.Run
import com.e.speedy.databinding.ListItemRunBinding

class RunAdapter(val runListener: RunListener): ListAdapter<Run, RunAdapter.ViewHolder>(RunDiffUtilCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, runListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemRunBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Run,
            runListener: RunListener
        ) {
            binding.run = item
            binding.clickListener = runListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRunBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class RunDiffUtilCallback: DiffUtil.ItemCallback<Run>() {
    override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
        return oldItem.runId == newItem.runId
    }

    override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
        return oldItem == newItem
    }
}

class RunListener(val clickListener: (runId: Long) -> Unit) {
    fun onClick(run: Run) = clickListener(run.runId)
}
