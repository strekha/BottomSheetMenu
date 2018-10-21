package com.strekha.bottomsheetmenu

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.strekha.bottomsheetmenu.BottomSheetMenu.Companion.GRID
import com.strekha.bottomsheetmenu.BottomSheetMenu.Companion.LIST

internal object LayoutManagerFactory {

    @JvmStatic
    fun provide(@BottomSheetMenu.Type type: Int,
                context: Context,
                adapter: MenuAdapter): RecyclerView.LayoutManager {
        return when(type) {
            LIST -> LinearLayoutManager(context)
            GRID -> {
                val spanCount = context.resources.getInteger(R.integer.grid_size)
                val gridLayoutManager = GridLayoutManager(context, spanCount)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return adapter.getItem(position).getWeight(spanCount)
                    }
                }
                return gridLayoutManager
            }
            else -> throw IllegalArgumentException("Unknown type! It must be one of LIST or GRID!")
        }
    }
}

