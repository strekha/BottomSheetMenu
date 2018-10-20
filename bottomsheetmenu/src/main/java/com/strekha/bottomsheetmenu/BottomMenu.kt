package com.strekha.bottomsheetmenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.internal.view.SupportMenuItem
import android.support.v7.content.res.AppCompatResources
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.view.menu.MenuItemImpl
import android.util.TypedValue
import android.view.MenuItem

import java.util.ArrayList

@SuppressLint("RestrictedApi")
internal class BottomMenu(context: Context, tintWrapper: TintWrapper) : MenuBuilder(context) {

    private val iconColorStateList: ColorStateList?

    val items: List<SupportMenuItem>
        get() {
            val visibleItems = visibleItems
            val items = ArrayList<SupportMenuItem>(visibleItems.size)
            items.addAll(visibleItems)
            return items
        }

    init {
        iconColorStateList = if (tintWrapper === TintWrapper.DEFAULT_TINT)
            createDefaultTint(android.R.attr.textColorSecondary)
        else
            tintWrapper.tint
    }

    override fun addInternal(group: Int, id: Int, categoryOrder: Int, title: CharSequence): MenuItem {
        val item = super.addInternal(group, id, categoryOrder, title) as SupportMenuItem
        item.iconTintList = iconColorStateList
        return item
    }

    private fun createDefaultTint(baseColorThemeAttr: Int): ColorStateList? {
        val value = TypedValue()
        if (!context.theme.resolveAttribute(baseColorThemeAttr, value, true)) {
            return null
        }
        val baseColor = AppCompatResources.getColorStateList(
                context,
                value.resourceId
        )
        return ColorStateList(
                arrayOf(EMPTY_STATE_SET),
                intArrayOf(baseColor.defaultColor)
        )
    }

    internal class TintWrapper(val tint: ColorStateList?) {

        companion object {
            val DEFAULT_TINT = TintWrapper(null)
        }
    }

    companion object {
        private val EMPTY_STATE_SET = intArrayOf()
    }
}
