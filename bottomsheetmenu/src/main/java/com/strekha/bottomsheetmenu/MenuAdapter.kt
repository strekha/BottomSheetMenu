package com.strekha.bottomsheetmenu

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup

internal class MenuAdapter(
        private val items: List<Item>,
        private val itemType: Int,
        private val listener: (MenuItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TITLE -> Holder.Title(inflater, parent, itemType)
            ELEMENT -> Holder.Element(inflater, parent, listener, itemType)
            SEPARATOR -> Holder.Separator(inflater, parent, itemType)
            else -> throw IllegalArgumentException("Unknown viewType!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            TITLE -> (holder as Holder.Title).bind(item as Item.Title)
            ELEMENT -> (holder as Holder.Element).bind(item as Item.Element)
            SEPARATOR -> { /* ignored */}
            else -> throw IllegalArgumentException("Unknown viewType!")
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is Item.Title -> TITLE
            is Item.Element -> ELEMENT
            is Item.Separator -> SEPARATOR
        }
    }

    fun getItem(position: Int): Item {
        return items[position]
    }

    companion object {

        private const val TITLE     = 101
        private const val ELEMENT   = 102
        private const val SEPARATOR = 103
    }
}
