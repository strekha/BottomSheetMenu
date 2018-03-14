package com.strekha.bottomsheetmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class MenuItemImpl implements MenuItem {

    private static final int NO_ICON = 0;

    private final Context mContext;
    private final int mGroup;
    private final int mId;
    private final int mOrdering;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    private Drawable mIconDrawable;
    private int mIconResId = NO_ICON;
    private Intent mIntent;
    private char mNumericShortcut;
    private char mAlphaShortcut;

    private int mFlags = ENABLED;
    private static final int CHECKABLE = 0x00000001;
    private static final int CHECKED = 0x00000002;
    private static final int EXCLUSIVE = 0x00000004;
    private static final int HIDDEN = 0x00000008;
    private static final int ENABLED = 0x00000010;

    private OnMenuItemClickListener mMenuItemClickListener;

    MenuItemImpl(@NonNull Context context,
                        int group,
                        @IdRes int id,
                        int ordering,
                        CharSequence title) {
        mContext = context;
        mGroup = group;
        mId = id;
        mOrdering = ordering;
        mTitle = title;
    }

    @Override
    public int getItemId() {
        return mId;
    }

    @Override
    public int getGroupId() {
        return mGroup;
    }

    @Override
    public int getOrder() {
        return mOrdering;
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    @Override
    public MenuItem setTitle(int title) {
        mTitle = mContext.getString(title);
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return mTitle;
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence titleCondensed) {
        mTitleCondensed = titleCondensed;
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        if (mTitleCondensed == null) return mTitle;
        else return mTitleCondensed;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        mIconDrawable = icon;
        mIconResId = NO_ICON;
        return this;
    }

    @Override
    public MenuItem setIcon(int iconRes) {
        mIconDrawable = null;
        mIconResId = iconRes;
        return this;
    }

    @Override
    public Drawable getIcon() {
        if (mIconDrawable != null) {
            return mIconDrawable;
        }

        if (mIconResId != NO_ICON) {
            return AppCompatResources.getDrawable(mContext, mIconResId);
        }

        return null;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        mIntent = intent;
        return this;
    }

    @Override
    public Intent getIntent() {
        return mIntent;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        mNumericShortcut = numericChar;
        mAlphaShortcut = Character.toLowerCase(alphaChar);
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        mNumericShortcut = numericChar;
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return mNumericShortcut;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        mAlphaShortcut = Character.toLowerCase(alphaChar);
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return mAlphaShortcut;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        mFlags = (mFlags & ~CHECKABLE) | (checkable ? CHECKABLE : 0);
        return this;
    }

    @Override
    public boolean isCheckable() {
        return (mFlags & CHECKABLE) == CHECKABLE;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        mFlags = (mFlags & ~CHECKED) | (checked ? CHECKED : 0);
        return this;
    }

    @Override
    public boolean isChecked() {
        return (mFlags & CHECKED) == CHECKED;
    }

    @Override
    public MenuItem setVisible(boolean visible) {
        mFlags = (mFlags & HIDDEN) | (visible ? 0 : HIDDEN);
        return this;
    }

    @Override
    public boolean isVisible() {
        return (mFlags & HIDDEN) == 0;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        mFlags = (mFlags & ~ENABLED) | (enabled ? ENABLED : 0);
        return this;
    }

    @Override
    public boolean isEnabled() {
        return (mFlags & ENABLED) != 0;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
        return null;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int actionEnum) {

    }

    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        setShowAsAction(actionEnum);
        return this;
    }

    @Override
    public MenuItem setActionView(View view) {
        throw new UnsupportedOperationException("Action view not supported!");
    }

    @Override
    public MenuItem setActionView(int resId) {
        throw new UnsupportedOperationException("Action view not supported!");
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException("Action view not supported!");
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        return null;
    }

    void setExclusiveCheckable(boolean exclusive) {
        mFlags = (mFlags & ~EXCLUSIVE) | (exclusive ? EXCLUSIVE : 0);
    }

    boolean invoke() {
        if (mMenuItemClickListener != null && mMenuItemClickListener.onMenuItemClick(this)) {
            return true;
        }

        if (mIntent != null) {
            mContext.startActivity(mIntent);
            return true;
        }

        return false;
    }


}
