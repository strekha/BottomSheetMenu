package com.strekha.bottomsheetmenu;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

abstract class Holder {

    static class Title extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        Title(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int type) {
            super(inflater.inflate(
                    type == BottomSheetMenu.LIST ? R.layout.item_title : R.layout.item_grid_title,
                    parent,
                    false
                  )
            );
            mTextView = itemView.findViewById(R.id.title_text_view);
        }

        void bind(@NonNull Item.Title item) {
            mTextView.setText(item.title);
        }
    }

    static class Element extends RecyclerView.ViewHolder {

        private final AppCompatImageView mIconView;
        private final TextView mTitleTextView;
        @NonNull
        private final BottomSheetMenu.OnBottomMenuListener mListener;
        private final int mPadding;

        Element(@NonNull LayoutInflater inflater,
                @NonNull ViewGroup parent,
                @NonNull BottomSheetMenu.OnBottomMenuListener listener,
                int type,
                @Nullable ColorStateList iconStateList) {
            super(inflater.inflate(
                    type == BottomSheetMenu.LIST ? R.layout.item_menu : R.layout.item_grid_menu,
                    parent,
                    false
                  )
            );
            mListener = listener;
            mIconView = itemView.findViewById(R.id.icon_image_view);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mPadding = type == BottomSheetMenu.LIST
                    ? 0
                    : parent.getResources().getDimensionPixelOffset(R.dimen.grid_padding_horizontal);

            ImageViewCompat.setImageTintList(mIconView, iconStateList);
        }

        void bind(@NonNull final Item.Element element) {
            Drawable icon = element.item.getIcon();
            if (icon == null) {
                mIconView.setVisibility(View.GONE);
            } else {
                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageDrawable(icon);
            }
            mTitleTextView.setText(element.item.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMenuItemSelected(element.item);
                }
            });

/*
            ViewCompat.setPaddingRelative(
                    itemView,
                    isLeft ? mPadding : 0,
                    itemView.getPaddingTop(),
                    isRight ? mPadding : 0,
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
