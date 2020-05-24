package com.e.speedy.runTracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.e.speedy.R
import com.e.speedy.database.Run
import com.e.speedy.databinding.ListItemRunBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1


class RunAdapter(val runListener: RunListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(RunDiffUtilCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val runItem = getItem(position) as DataItem.RunItem
                holder.bind(runItem.run, runListener)
            }
        }
    }

    fun addHeaderSubmitList(list: List<Run>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.RunItem(it)}
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.RunItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown view type ${viewType}")
        }
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

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class RunDiffUtilCallback: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class RunListener(val clickListener: (runId: Long) -> Unit) {
    fun onClick(run: Run) = clickListener(run.runId)
}

sealed class DataItem {
    data class RunItem(val run: Run): DataItem() {
        override val id = run.runId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
