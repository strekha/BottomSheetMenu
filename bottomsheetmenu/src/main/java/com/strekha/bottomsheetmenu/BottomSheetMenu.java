package com.strekha.bottomsheetmenu;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;

public final class BottomSheetMenu {

    public static final int LIST = 34;
    public static final int GRID = 35;
    private static final int SPAN_COUNT = 3;

    @NonNull
    private final Context mContext;
    @NonNull
    private final Builder mBuilder;
    @NonNull
    private final MenuBuilder mMenu;
    private LayoutInflater mLayoutInflater;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIST, GRID})
    public @interface Type {}

    private BottomSheetMenu(@NonNull Builder builder) {
        mContext = builder.context;
        mBuilder = builder;
        mMenu = getMenu();
        mLayoutInflater = LayoutInflater.from(mContext);
        getMenuInflater().inflate(builder.menuRes, mMenu);
    }

    public void show() {
        final BaseBottomDialog dialog = new BaseBottomDialog(mContext);

        if (mBuilder.type != LIST && mBuilder.type != GRID) {
            throw new IllegalArgumentException("Unknown type! It must be one of LIST or GRID!");
        }

        List<Item> items = mapToItems(mBuilder.title, mMenu.getItems());
        MenuAdapter adapter = new MenuAdapter(
                items,
                new OnBottomMenuListener() {
                    @Override
                    public void onMenuItemSelected(@NonNull MenuItem item) {
                        dialog.hide();
                        if (mBuilder.listener != null) {
                            mBuilder.listener.onMenuItemSelected(item);
                        }
                    }
                },
                mBuilder.type,
                SPAN_COUNT
        );

        RecyclerView recyclerView = (RecyclerView) mLayoutInflater.inflate(R.layout.list_menu, null);
        recyclerView.setLayoutManager(getLayoutManager(adapter));
        recyclerView.setAdapter(adapter);
        setupPadding(recyclerView);
        dialog.setContentView(recyclerView);
        dialog.show();
    }

    @NonNull
    private RecyclerView.LayoutManager getLayoutManager(@NonNull final MenuAdapter adapter) {
        if (mBuilder.type == LIST) {
            return new LinearLayoutManager(mContext);
        } else if (mBuilder.type == GRID) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, SPAN_COUNT);
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

    @NonNull
    private List<Item> mapToItems(@Nullable CharSequence title,
                                  @NonNull List<MenuItem> menuItems) {
        List<Item> items = new ArrayList<>();
        if (title != null) {
            items.add(new Item.Title(title));
        }

        int prevGroup = -1;
        for (MenuItem menuItem : menuItems) {
            int currentGroup = menuItem.getGroupId();
            if (currentGroup != prevGroup && prevGroup != -1) {
                items.add(new Item.Separator());
            }
            prevGroup = currentGroup;
            items.add(new Item.Element(menuItem));
        }

        return items;
    }

    private void setupPadding(@NonNull RecyclerView recyclerView) {

        int horizontal = 0;

        int bottom = mBuilder.type == LIST
                ? mContext.getResources().getDimensionPixelOffset(R.dimen.list_padding_vertical)
                : 0;

        int top = 0;
        if (mBuilder.title == null) {
            top = mBuilder.type == LIST
                    ? mContext.getResources().getDimensionPixelOffset(R.dimen.list_padding_vertical)
                    : mContext.getResources().getDimensionPixelOffset(R.dimen.grid_padding_vertical);
        }

        ViewCompat.setPaddingRelative(recyclerView, horizontal, top, horizontal, bottom);

    }

    @NonNull
    private MenuInflater getMenuInflater() {
        return new MenuInflater(mContext);
    }

    @NonNull
    private MenuBuilder getMenu() {
        return new MenuBuilder(mContext);
    }

    public static class Builder {

        @NonNull
        private final Context context;
        private int menuRes;
        private CharSequence title;
        private OnBottomMenuListener listener;
        private int type = LIST;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        @NonNull
        public Builder inflate(@MenuRes int menuRes) {
            this.menuRes = menuRes;
            return this;
        }

        @NonNull
        public Builder withTitle(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        @NonNull
        public Builder withTitle(@StringRes int titleRes) {
            this.title = context.getString(titleRes);
            return this;
        }

        @NonNull
        public Builder withListener(@NonNull OnBottomMenuListener listener) {
            this.listener = listener;
            return this;
        }

        @NonNull
        public Builder withType(@Type int type) {
            this.type = type;
            return this;
        }

        @NonNull
        public BottomSheetMenu create() {
            return new BottomSheetMenu(this);
        }

        public void show() {
            create().show();
        }
    }

    public interface OnBottomMenuListener {
        void onMenuItemSelected(@NonNull MenuItem item);
    }
}
