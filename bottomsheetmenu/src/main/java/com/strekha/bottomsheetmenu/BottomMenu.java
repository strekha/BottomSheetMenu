package com.strekha.bottomsheetmenu;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.TypedValue;
import android.view.MenuItem;

@SuppressLint("RestrictedApi")
class BottomMenu extends MenuBuilder {

    private static final int[] EMPTY_STATE_SET = {};

    @Nullable
    private ColorStateList mIconColorStateList;

    BottomMenu(@NonNull Context context, @NonNull TintWrapper tintWrapper) {
        super(context);
        if (tintWrapper == TintWrapper.DEFAULT_TINT) {
            mIconColorStateList = createDefaultColorStateList(android.R.attr.textColorSecondary);
        } else {
            mIconColorStateList = null;
        }
    }

    @Override
    protected MenuItem addInternal(int group, int id, int categoryOrder, CharSequence title) {
        SupportMenuItem item = ((SupportMenuItem) super.addInternal(group, id, categoryOrder, title));
        item.setIconTintList(mIconColorStateList);
        return item;
    }

    @NonNull
    List<SupportMenuItem> getItems() {
        List<MenuItemImpl> visibleItems = getVisibleItems();
        List<SupportMenuItem> items = new ArrayList<>(visibleItems.size());
        items.addAll(visibleItems);
        return items;
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        final TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = AppCompatResources.getColorStateList(
                getContext(),
                value.resourceId
        );
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{
                EMPTY_STATE_SET
        }, new int[]{
                defaultColor
        });
    }

    static class TintWrapper {

        static final TintWrapper DEFAULT_TINT = new TintWrapper(null);

        @Nullable
        final ColorStateList tint;

        TintWrapper(@Nullable ColorStateList tint) {
            this.tint = tint;
        }
    }
}
