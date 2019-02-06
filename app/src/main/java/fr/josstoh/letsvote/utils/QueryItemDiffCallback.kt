package fr.josstoh.letsvote.utils

import androidx.recyclerview.widget.DiffUtil
import fr.josstoh.letsvote.common.QueryItem

/**
 * Utility for diffing lists of QueryItem elements for use with RecyclerView.
 * This code makes the assumption that the generic type T is a data class,
 * which has an automatically accurate equals() implementation.
 */
open class QueryItemDiffCallback<T> : DiffUtil.ItemCallback<QueryItem<T>>() {
    override fun areItemsTheSame(oldItem: QueryItem<T>, newItem: QueryItem<T>): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: QueryItem<T>, newItem: QueryItem<T>): Boolean {
        return oldItem.item == newItem.item
    }
}