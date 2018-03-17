package com.strekha.bottomsheetmenu;

import static com.strekha.bottomsheetmenu.BottomSheetMenu.GRID;
import static com.strekha.bottomsheetmenu.BottomSheetMenu.LIST;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

final class LayoutManagerFactory {

    static final int SPAN_COUNT = 3;

    private LayoutManagerFactory() {
    }

    @NonNull
    static RecyclerView.LayoutManager getLayoutManager(@BottomSheetMenu.Type int type,
                                                       @NonNull final Context context,
                                                       @NonNull final MenuAdapter adapter) {
        if (type == LIST) {
            return new LinearLayoutManager(context);
        } else if (type == GRID) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, SPAN_COUNT);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.getItem(position).getWeight(SPAN_COUNT);
                }
            });
            return gridLayoutManager;
        }
        throw new IllegalArgumentException("Unknown type! It must be one of LIST or GRID!");
    }
}

