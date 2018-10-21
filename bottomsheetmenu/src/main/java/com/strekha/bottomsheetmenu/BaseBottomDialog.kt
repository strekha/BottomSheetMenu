package com.strekha.bottomsheetmenu

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.ViewGroup
import android.widget.FrameLayout

class BaseBottomDialog internal constructor(context: Context, @StyleRes theme: Int)
    : BottomSheetDialog(context, theme) {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val isTablet = context.resources.getBoolean(R.bool.isTablet)
        window?.setLayout(getWidth(isTablet), ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun getWidth(isTablet: Boolean): Int {
        return if (isTablet) {
            context.resources.getDimensionPixelOffset(R.dimen.bottom_menu_menu_max_width)
        } else {
            ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    override fun onStart() {
        super.onStart()
        val orientation = context.resources.configuration.orientation
        val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            val sheet = findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val sheetBehavior = BottomSheetBehavior.from(sheet!!)
            sheet.post { sheetBehavior.peekHeight = sheet.height / 2 }
        }
    }
}
