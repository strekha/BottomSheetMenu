package com.strekha.bottomsheetmenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public class MenuBuilder implements Menu {

    private static final int NO_INDEX = -1;

    @NonNull
    private final Context mContext;
    @NonNull
    private final List<MenuItemImpl> mItems = new ArrayList<>();
    private boolean mIsQwerty;

    MenuBuilder(@NonNull Context context) {
        mContext = context;
    }

    List<MenuItem> getItems() {
        List<MenuItem> items = new ArrayList<>(mItems.size());
        items.addAll(mItems);
        return items;
    }

    @Override
    public MenuItem add(CharSequence title) {
        return add(0, 0, 0, title);
    }

    @Override
    public MenuItem add(int titleRes) {
        return add(0, 0, 0, titleRes);
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        MenuItemImpl item = new MenuItemImpl(
                mContext,
                groupId,
                itemId,
                order,
                title
        );
        mItems.add(item);
        return item;
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return add(groupId, itemId, order, mContext.getString(titleRes));
    }

    @Override
    public SubMenu addSubMenu(CharSequence title) {
        throw new UnsupportedOperationException("SubMenu not supported!");
    }

    @Override
    public SubMenu addSubMenu(int titleRes) {
        throw new UnsupportedOperationException("SubMenu not supported!");
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        throw new UnsupportedOperationException("SubMenu not supported!");
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        throw new UnsupportedOperationException("SubMenu not supported!");
    }

    @Override
    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller,
                                Intent[] specifics, Intent intent, int flags,
                                MenuItem[] outSpecificItems) {
        PackageManager pm = mContext.getPackageManager();
        final List<ResolveInfo> lri =
                pm.queryIntentActivityOptions(caller, specifics, intent, 0);
        final int N = lri != null ? lri.size() : 0;

        if ((flags & FLAG_APPEND_TO_GROUP) == 0) {
            removeGroup(groupId);
        }

        for (int i = 0; i < N; i++) {
            final ResolveInfo ri = lri.get(i);
            Intent rintent = new Intent(
                    ri.specificIndex < 0 ? intent : specifics[ri.specificIndex]);
            rintent.setComponent(new ComponentName(
                    ri.activityInfo.applicationInfo.packageName,
                    ri.activityInfo.name));
            final MenuItem item = add(groupId, itemId, order, ri.loadLabel(pm))
                    .setIcon(ri.loadIcon(pm))
                    .setIntent(rintent);
            if (outSpecificItems != null && ri.specificIndex >= 0) {
                outSpecificItems[ri.specificIndex] = item;
            }
        }

        return N;
    }

    @Override
    public void removeItem(int id) {
        int itemIndex = findItemIndex(id);
        if (itemIndex == NO_INDEX) return;

        mItems.remove(itemIndex);
    }

    @Override
    public void removeGroup(int groupId) {
        for (Iterator<MenuItemImpl> iterator = mItems.iterator(); iterator.hasNext();) {
            MenuItemImpl item = iterator.next();
            if (item.getGroupId() == groupId) {
                iterator.remove();
            }
        }
    }

    @Override
    public void clear() {
        mItems.clear();
    }

    @Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        for (MenuItemImpl item : mItems) {
            if (item.getGroupId() == group) {
                item.setCheckable(checkable);
                item.setExclusiveCheckable(exclusive);
            }
        }
    }

    @Override
    public void setGroupVisible(int group, boolean visible) {
        for (MenuItemImpl item : mItems) {
            if (item.getGroupId() == group) {
                item.setVisible(visible);
            }
        }
    }

    @Override
    public void setGroupEnabled(int group, boolean enabled) {
        for (MenuItemImpl item : mItems) {
            if (item.getGroupId() == group) {
                item.setEnabled(enabled);
            }
        }
    }

    @Override
    public boolean hasVisibleItems() {
        for (MenuItemImpl item : mItems) {
            if (item.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MenuItem findItem(int id) {
        for (MenuItemImpl item : mItems) {
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return mItems.size();
    }

    @Override
    public MenuItem getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        final MenuItemImpl item = findItemWithShortcutForKey(keyCode, event);
        return item != null && item.invoke();
    }

    @Override
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return findItemWithShortcutForKey(keyCode, event) != null;
    }

    @Override
    public boolean performIdentifierAction(int id, int flags) {
        int itemIndex = findItemIndex(id);
        return itemIndex != NO_INDEX && mItems.get(itemIndex).invoke();

    }

    @Override
    public void setQwertyMode(boolean isQwerty) {
        mIsQwerty = isQwerty;
    }

    private int findItemIndex(int id) {
        for (int i = 0, size = mItems.size(); i < size; i++) {
            if (mItems.get(i).getItemId() == id) {
                return i;
            }
        }
        return NO_INDEX;
    }

    private MenuItemImpl findItemWithShortcutForKey(int keyCode, KeyEvent event) {
        for (MenuItemImpl item : mItems) {
            char shortcut = mIsQwerty ? item.getAlphabeticShortcut() : item.getNumericShortcut();
            if (keyCode == shortcut) return item;
        }
        return null;
    }
}
