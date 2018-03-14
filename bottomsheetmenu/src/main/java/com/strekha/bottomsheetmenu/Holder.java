package com.strekha.bottomsheetmenu;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

class Holder {

    private Holder() {
    }

    static class Title extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public Title(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_title, parent, false));
            mTextView = itemView.findViewById(R.id.title_text_view);
        }

        void bind(@NonNull Item.Title item) {
            mTextView.setText(item.title);
        }
    }

    static class Element extends RecyclerView.ViewHolder {

        private final ImageView mIconView;
        private final TextView mTitleTextView;
        @NonNull
        private final BottomSheetMenu.OnBottomMenuListener mListener;

        public Element(@NonNull LayoutInflater inflater,
                       @NonNull ViewGroup parent,
                       @NonNull BottomSheetMenu.OnBottomMenuListener listener) {
            super(inflater.inflate(R.layout.item_menu, parent, false));
            mListener = listener;
            mIconView = itemView.findViewById(R.id.icon_image_view);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
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
        }
    }

    static class Separator extends RecyclerView.ViewHolder {

        public Separator(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_divider, parent, false));
        }
    }
}
