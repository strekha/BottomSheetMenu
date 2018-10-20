package com.strekha.bottomsheetmenu;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

@SuppressLint("RestrictedApi")
abstract class Holder {

    static class Title extends RecyclerView.ViewHolder {

        private final TextView textView;

        Title(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int type) {
            super(inflater.inflate(
                    type == BottomSheetMenu.LIST ? R.layout.item_title : R.layout.item_grid_title,
                    parent,
                    false
                  )
            );
            textView = itemView.findViewById(R.id.title_text_view);
        }

        void bind(@NonNull Item.Title item) {
            textView.setText(item.getTitle());
        }
    }

    static class Element extends RecyclerView.ViewHolder {

        private final AppCompatImageView iconView;
        private final TextView titleTextView;
        private final Function1<MenuItem, Unit> listener;
        private final int padding;

        Element(@NonNull LayoutInflater inflater,
                @NonNull ViewGroup parent,
                @NonNull Function1<MenuItem, Unit> listener,
                int type) {
            super(inflater.inflate(
                    type == BottomSheetMenu.LIST ? R.layout.item_menu : R.layout.item_grid_menu,
                    parent,
                    false
                  )
            );
            this.listener = listener;
            iconView = itemView.findViewById(R.id.icon_image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            padding = type == BottomSheetMenu.LIST
                    ? 0
                    : parent.getResources().getDimensionPixelOffset(R.dimen.bottom_menu_grid_padding_horizontal);
        }

        @SuppressLint("RestrictedApi")
        void bind(@NonNull final Item.Element element) {
            Drawable icon = element.getItem().getIcon();
            if (icon == null) {
                iconView.setVisibility(View.GONE);
            } else {
                iconView.setVisibility(View.VISIBLE);
                iconView.setImageDrawable(icon);
            }
            titleTextView.setText(element.getItem().getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.invoke(element.getItem());
                }
            });

            ImageViewCompat.setImageTintList(iconView, element.getItem().getIconTintList());
/*
            ViewCompat.setPaddingRelative(
                    itemView,
                    isLeft ? padding : 0,
                    itemView.getPaddingTop(),
                    isRight ? padding : 0,
                    itemView.getPaddingBottom()
            );
*/
        }
    }

    static class Separator extends RecyclerView.ViewHolder {

        Separator(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int type) {
            super(inflater.inflate(
                    type == BottomSheetMenu.LIST ? R.layout.item_divider : R.layout.item_grid_divider,
                    parent,
                    false
                  )
            );
        }
    }
}
