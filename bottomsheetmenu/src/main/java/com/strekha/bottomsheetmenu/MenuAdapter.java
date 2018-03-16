package com.strekha.bottomsheetmenu;

import java.util.List;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

final class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TITLE = 101;
    private static final int ELEMENT = 102;
    private static final int SEPARATOR = 103;

    private final List<Item> mItems;
    private final BottomSheetMenu.OnBottomMenuListener mListener;
    private final int mItemType;
    private final ColorStateList mIconTint;

    MenuAdapter(@NonNull List<Item> items,
                @NonNull BottomSheetMenu.OnBottomMenuListener listener,
                int itemType,
                ColorStateList iconTint) {
        mItems = items;
        mListener = listener;
        mItemType = itemType;
        mIconTint = iconTint;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TITLE:
                return new Holder.Title(inflater, parent, mItemType);
            case ELEMENT:
                return new Holder.Element(inflater, parent, mListener, mItemType, mIconTint);
            case SEPARATOR:
                return new Holder.Separator(inflater, parent, mItemType);
            default:
                throw new IllegalArgumentException("Unknown viewType!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = mItems.get(position);
        switch (getItemViewType(position)) {
            case TITLE:
                ((Holder.Title) holder).bind((Item.Title) item);
                break;
            case ELEMENT:
                ((Holder.Element) holder).bind(((Item.Element) item));
                break;
            case SEPARATOR:
                break;
            default:
                throw new IllegalArgumentException("Unknown viewType!");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Item item = mItems.get(position);
        if (item instanceof Item.Title) {
            return TITLE;
        } else if (item instanceof Item.Element) {
            return ELEMENT;
        } else if (item instanceof Item.Separator) {
            return SEPARATOR;
        }
        throw new IllegalArgumentException("Unknown viewType!");
    }

    @NonNull
    Item getItem(int position) {
        return  mItems.get(position);
    }
}
