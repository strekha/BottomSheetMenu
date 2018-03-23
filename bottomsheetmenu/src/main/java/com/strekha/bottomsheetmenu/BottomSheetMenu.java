package com.strekha.bottomsheetmenu;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Represents a menu, which shows at the bottom of the screen.
 * The menu contents can be populated by a menu resource file.
 * <p>
 * Class supports both list and grid types.
 */
public final class BottomSheetMenu {

    public static final int LIST = 34;
    public static final int GRID = 35;

    @NonNull
    private final Context mContext;
    @NonNull
    private final Builder mBuilder;
    @NonNull
    private final BottomMenu mMenu;
    private LayoutInflater mLayoutInflater;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIST, GRID})
    public @interface Type {}

    private BottomSheetMenu(@NonNull Builder builder) {
        mContext = builder.context;
        mBuilder = builder;
        mMenu = new BottomMenu(mContext, builder.iconTint);
        mLayoutInflater = LayoutInflater.from(mContext);
        getMenuInflater().inflate(builder.menuRes, mMenu);
        if (mBuilder.menuConsumer != null) {
            mBuilder.menuConsumer.accept(mMenu);
        }
    }

    /**
     * Shows the menu anchored to the bottom of the screen.
     */
    public void show() {
        final BaseBottomDialog dialog = new BaseBottomDialog(mContext, getTheme(mBuilder.style));

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
                mBuilder.type
        );

        RecyclerView recyclerView = (RecyclerView) mLayoutInflater.inflate(R.layout.bottom_sheet_menu, null);
        recyclerView.setLayoutManager(
                LayoutManagerFactory.getLayoutManager(mBuilder.type, mContext, adapter)
        );
        recyclerView.setAdapter(adapter);
        setupPadding(recyclerView);
        dialog.setContentView(recyclerView);
        dialog.show();
    }

    @NonNull
    private List<Item> mapToItems(@Nullable CharSequence title,
                                  @NonNull List<SupportMenuItem> menuItems) {
        List<Item> items = new ArrayList<>();
        if (title != null) {
            items.add(new Item.Title(title));
        }

        int prevGroup = -1;
        for (SupportMenuItem menuItem : menuItems) {
            int currentGroup = menuItem.getGroupId();
            if (currentGroup != prevGroup && prevGroup != -1) {
                items.add(new Item.Separator());
            }
            prevGroup = currentGroup;
            items.add(new Item.Element(menuItem));
        }

        return items;
    }

    @StyleRes
    private int getTheme(int style) {
        if (style == 0) {
            int[] attr = new int[]{android.support.v7.appcompat.R.attr.isLightTheme};
            TypedArray typedArray = mContext.obtainStyledAttributes(attr);
            boolean isLight = typedArray.getBoolean(0, false);
            typedArray.recycle();

            return isLight
                    ? R.style.Theme_Design_Light_BottomSheetDialog
                    : R.style.Theme_Design_BottomSheetDialog;
        } else return style;
    }

    private void setupPadding(@NonNull RecyclerView recyclerView) {

        int horizontal = 0;

        int bottom = mBuilder.type == LIST
                ? mContext.getResources().getDimensionPixelOffset(R.dimen.bottom_menu_list_padding_vertical)
                : 0;

        int top = 0;
        if (mBuilder.title == null) {
            top = mBuilder.type == LIST
                    ? mContext.getResources().getDimensionPixelOffset(R.dimen.bottom_menu_list_padding_vertical)
                    : mContext.getResources().getDimensionPixelOffset(R.dimen.bottom_menu_grid_padding_vertical);
        }

        ViewCompat.setPaddingRelative(recyclerView, horizontal, top, horizontal, bottom);

    }

    @SuppressLint("RestrictedApi")
    @NonNull
    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(mContext);
    }

    public static class Builder {

        @NonNull
        private Context context;
        private int menuRes;
        @Nullable
        private CharSequence title;
        @Nullable
        private OnBottomMenuListener listener;
        private int type = LIST;
        @NonNull
        private BottomMenu.TintWrapper iconTint = BottomMenu.TintWrapper.DEFAULT_TINT;
        private int style;
        @Nullable
        private MenuConsumer menuConsumer;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        /**
         * Inflate a menu resource into this BottomSheetMenu.
         *
         * @param menuRes Menu resource to inflate
         */
        @NonNull
        public Builder inflate(@MenuRes int menuRes) {
            this.menuRes = menuRes;
            return this;
        }

        /**
         * Adds a title to the menu.
         * By default menu has not any title.
         *
         * @param title text to be shown at the top of menu
         */
        @NonNull
        public Builder withTitle(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        /**
         * @see #withTitle(CharSequence)
         */
        @NonNull
        public Builder withTitle(@StringRes int titleRes) {
            this.title = context.getString(titleRes);
            return this;
        }

        /**
         * Sets a listener that will be notified when the user selects an item from
         * the menu.
         *
         * @param listener the listener to notify
         */
        @NonNull
        public Builder withListener(@NonNull OnBottomMenuListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Sets the type of menu.
         * Should be one of {@link #LIST} or {@link #GRID}.
         *
         * @param type the type of menu
         */
        @NonNull
        public Builder withType(@Type int type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the the tint, that will be applied to icons.
         * Default tint is depends on theme - {@link android.R.attr#textColorSecondary} will be used.
         *
         * @param iconTint the tint, that will be applied to icons.
         */
        @NonNull
        public Builder withIconTint(@ColorRes int iconTint) {
            return withIconTint(AppCompatResources.getColorStateList(context, iconTint));
        }

        /**
         * @see #withIconTint(int)
         */
        @NonNull
        public Builder withIconTint(@Nullable ColorStateList iconTint) {
            this.iconTint = new BottomMenu.TintWrapper(iconTint);
            return this;
        }

        /**
         * Sets the style, which will be applied
         * to {@link android.support.design.widget.BottomSheetDialog}.
         * <p>
         * Make sure the style passed to method
         * extends {@link R.style#Theme_Design_BottomSheetDialog} family.
         *
         * @param style style to apply to BottomSheetDialog
         */
        @NonNull
        public Builder withStyle(@StyleRes int style) {
            this.style = style;
            return this;
        }

        /**
         * This method allow to modify menu items after inflating resource file.
         * <p>
         * This can be useful if you wand to disable default icon tint for some items:
         * <pre>
         *     new BottomSheetMenu.Builder(this)
         *          .inflate(R.menu.sample)
         *          .mapMenu(menu -> {
         *              MenuItemCompat.setIconTintList(menu.findItem(R.id.email), null);
         *          })
         *          .show();
         * </pre>
         *
         * @param menuConsumer callback, that will be invoked after inflating.
         */
        @NonNull
        public Builder mapMenu(@Nullable MenuConsumer menuConsumer) {
            this.menuConsumer = menuConsumer;
            return this;
        }

        /**
         * @return created {@link BottomSheetMenu} object
         */
        @NonNull
        public BottomSheetMenu build() {
            return new BottomSheetMenu(this);
        }

        /**
         * Creates and shows {@link BottomSheetMenu}.
         * @see BottomSheetMenu#show()
         */
        public void show() {
            build().show();
        }
    }

    /**
     * Interface responsible for receiving menu item click events.
     */
    public interface OnBottomMenuListener {

        /**
         * This method will be invoked when a menu item is clicked.
         *
         * @param item the menu item that was clicked
         */

        void onMenuItemSelected(@NonNull MenuItem item);
    }

    /**
     * Interface responsible for receiving and consuming menu after inflating.
     */
    public interface MenuConsumer {

        /**
         * This method will be invoked after inflating the menu resource file.
         *
         * @param menu menu item, which was inflated from Xml resource
         */
        void accept(@NonNull Menu menu);
    }
}
