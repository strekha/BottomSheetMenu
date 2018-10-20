package com.strekha.bottomsheetmenu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

final class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TITLE = 101;
    private static final int ELEMENT = 102;
    private static final int SEPARATOR = 103;

    private final List<Item> items;
    private final BottomSheetMenu.OnBottomMenuListener listener;
    private final int itemType;

    MenuAdapter(@NonNull List<Item> items,
                @NonNull BottomSheetMenu.OnBottomMenuListener listener,
                int itemType) {
        this.items = items;
        this.listener = listener;
        this.itemType = itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TITLE:
                return new Holder.Title(inflater, parent, itemType);
            case ELEMENT:
                return new Holder.Element(inflater, parent, listener, itemType);
            case SEPARATOR:
                return new Holder.Separator(inflater, parent, itemType);
            default:
                throw new IllegalArgumentException("Unknown viewType!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
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
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Item item = items.get(position);
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
        return items.get(position);
    }
}
