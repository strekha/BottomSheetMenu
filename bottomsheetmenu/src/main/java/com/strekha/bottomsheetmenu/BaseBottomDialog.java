package com.strekha.bottomsheetmenu;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class BaseBottomDialog extends BottomSheetDialog {

    BaseBottomDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            if (!getContext().getResources().getBoolean(R.bool.isTablet)) {
                window.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
            } else {
                window.setLayout(
                        getContext().getResources().getDimensionPixelOffset(R.dimen.menu_max_height),
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onStart() {
        super.onStart();
        boolean isLandscape = getContext().getResources().getBoolean(R.bool.isLandscape);
        if (isLandscape) {
            final FrameLayout sheet = findViewById(R.id.design_bottom_sheet);
            final BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(sheet);
            sheet.post(new Runnable() {
                @Override
                public void run() {
                    sheetBehavior.setPeekHeight(sheet.getHeight() / 2);
                }
            });
        }
    }
}
