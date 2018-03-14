package com.strekha.bottomsheetmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;

public final class BottomSheetMenu {

    @NonNull
    private final Context mContext;
    @NonNull
    private final Builder mBuilder;
    @NonNull
    private final MenuBuilder mMenu;
    private int mVerticalPaddingPx;
    private LayoutInflater mLayoutInflater;

    private BottomSheetMenu(@NonNull Builder builder) {
        mContext = builder.context;
        mBuilder = builder;
        mVerticalPaddingPx = mContext.getResources().getDimensionPixelOffset(R.dimen.list_padding_vertical);
        mMenu = getMenu();
        mLayoutInflater = LayoutInflater.from(mContext);
        getMenuInflater().inflate(builder.menuRes, mMenu);
    }

    public void show() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);

        RecyclerView recyclerView = (RecyclerView) mLayoutInflater.inflate(R.layout.list_menu, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<Item> items = mapToItems(
                mBuilder.title,
                mMenu.getItems()
        );
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
                }
        );
        recyclerView.setAdapter(adapter);
        ViewCompat.setPaddingRelative(
                recyclerView,
                0,
                mBuilder.title == null ? mVerticalPaddingPx : 0,
                0,
                mVerticalPaddingPx
        );
        
        dialog.setContentView(recyclerView);
        dialog.show();
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
