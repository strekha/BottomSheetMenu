package com.strekha.bottomsheetmenu

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout

class BaseBottomDialog internal constructor(context: Context, @StyleRes theme: Int)
    : BottomSheetDialog(context, theme) {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        window?.let {
            if (Build.VERSION.SDK_INT >= 21) {
                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if (!context.resources.getBoolean(R.bool.isTablet)) {
                it.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
            } else {
                it.setLayout(
                        context.resources.getDimensionPixelOffset(R.dimen.bottom_menu_menu_max_width),
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
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
