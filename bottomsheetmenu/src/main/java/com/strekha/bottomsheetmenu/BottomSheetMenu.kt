package com.strekha.bottomsheetmenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.*
import android.support.v4.internal.view.SupportMenuItem
import android.support.v4.view.ViewCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.view.SupportMenuInflater
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import java.util.*

/**
 * Represents a menu, which shows at the bottom of the screen.
 * The menu contents can be populated by a menu resource file.
 *
 *
 * Class supports both list and grid types.
 */
@SuppressLint("RestrictedApi")
class BottomSheetMenu private constructor(private val builder: Builder) {

    private val context: Context
    private val menu: BottomMenu
    private val layoutInflater: LayoutInflater

    private val menuInflater: MenuInflater
        get() = SupportMenuInflater(context)

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(LIST, GRID)
    annotation class Type

    init {
        context = builder.context
        menu = BottomMenu(context, builder.iconTint)
        layoutInflater = LayoutInflater.from(context)
        menuInflater.inflate(builder.menuRes, menu)
        builder.menuConsumer?.accept(menu)
    }

    /**
     * Shows the menu anchored to the bottom of the screen.
     */
    fun show() {
        val dialog = BaseBottomDialog(context, getTheme(builder.style))

        if (builder.type != LIST && builder.type != GRID) {
            throw IllegalArgumentException("Unknown type! It must be one of LIST or GRID!")
        }

        val items = mapToItems(builder.title, menu.items)
        val adapter = MenuAdapter(items, builder.type ) {
            dialog.hide()
            builder.listener?.onMenuItemSelected(it)
        }

        val recyclerView = layoutInflater.inflate(R.layout.bottom_sheet_menu, null) as RecyclerView
        recyclerView.layoutManager = LayoutManagerFactory.provide(builder.type, context, adapter)
        recyclerView.adapter = adapter
        setupPadding(recyclerView)
        dialog.setContentView(recyclerView)
        dialog.show()
    }

    private fun mapToItems(title: CharSequence?,
                           menuItems: List<SupportMenuItem>): List<Item> {
        val items = ArrayList<Item>()
        title?.let { items.add(Item.Title(it)) }

        var prevGroup = -1
        for (menuItem in menuItems) {
            val currentGroup = menuItem.groupId
            if (currentGroup != prevGroup && prevGroup != -1) {
                items.add(Item.Separator)
            }
            prevGroup = currentGroup
            items.add(Item.Element(menuItem))
        }

        return items
    }

    @StyleRes
    private fun getTheme(style: Int): Int {
        if (style == 0) {
            val attr = intArrayOf(android.support.v7.appcompat.R.attr.isLightTheme)
            val typedArray = context.obtainStyledAttributes(attr)
            val isLight = typedArray.getBoolean(0, false)
            typedArray.recycle()

            return if (isLight)
                R.style.Theme_Design_Light_BottomSheetDialog
            else
                R.style.Theme_Design_BottomSheetDialog
        }
        return style
    }

    private fun setupPadding(recyclerView: RecyclerView) {

        val horizontal = 0

        val bottom = if (builder.type == LIST)
            context.resources.getDimensionPixelOffset(R.dimen.bottom_menu_list_padding_vertical)
        else
            0

        var top = 0
        if (builder.title == null) {
            top = if (builder.type == LIST)
                context.resources.getDimensionPixelOffset(R.dimen.bottom_menu_list_padding_vertical)
            else
                context.resources.getDimensionPixelOffset(R.dimen.bottom_menu_grid_padding_vertical)
        }

        ViewCompat.setPaddingRelative(recyclerView, horizontal, top, horizontal, bottom)

    }

    class Builder(internal val context: Context) {

        internal var menuRes = 0
        internal var title: CharSequence? = null
        internal var listener: OnBottomMenuListener? = null
        internal var type = LIST
        internal var iconTint = BottomMenu.TintWrapper.DEFAULT_TINT
        internal var style = 0
        internal var menuConsumer: MenuConsumer? = null

        /**
         * Inflate a menu resource into this BottomSheetMenu.
         *
         * @param menuRes Menu resource to inflate
         */
        fun inflate(@MenuRes menuRes: Int): Builder {
            this.menuRes = menuRes
            return this
        }

        /**
         * Adds a title to the menu.
         * By default menu has not any title.
         *
         * @param title text to be shown at the top of menu
         */
        fun withTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        /**
         * @see .withTitle
         */
        fun withTitle(@StringRes titleRes: Int): Builder {
            this.title = context.getString(titleRes)
            return this
        }

        /**
         * Sets a listener that will be notified when the user selects an item from
         * the menu.
         *
         * @param listener the listener to notify
         */
        fun withListener(listener: OnBottomMenuListener): Builder {
            this.listener = listener
            return this
        }

        /**
         * Sets the type of menu.
         * Should be one of [.LIST] or [.GRID].
         *
         * @param type the type of menu
         */
        fun withType(@Type type: Int): Builder {
            this.type = type
            return this
        }

        /**
         * Sets the the tint, that will be applied to icons.
         * Default tint is depends on theme - [android.R.attr.textColorSecondary] will be used.
         *
         * @param iconTint the tint, that will be applied to icons.
         */
        fun withIconTint(@ColorRes iconTint: Int): Builder {
            return withIconTint(AppCompatResources.getColorStateList(context, iconTint))
        }

        /**
         * @see .withIconTint
         */
        fun withIconTint(iconTint: ColorStateList?): Builder {
            this.iconTint = BottomMenu.TintWrapper(iconTint)
            return this
        }

        /**
         * Sets the style, which will be applied
         * to [android.support.design.widget.BottomSheetDialog].
         *
         *
         * Make sure the style passed to method
         * extends [R.style.Theme_Design_BottomSheetDialog] family.
         *
         * @param style style to apply to BottomSheetDialog
         */
        fun withStyle(@StyleRes style: Int): Builder {
            this.style = style
            return this
        }

        /**
         * This method allow to modify menu items after inflating resource file.
         *
         *
         * This can be useful if you wand to disable default icon tint for some items:
         * <pre>
         * new BottomSheetMenu.Builder(this)
         * .inflate(R.menu.sample)
         * .mapMenu(menu -> {
         * MenuItemCompat.setIconTintList(menu.findItem(R.id.email), null);
         * })
         * .show();
        </pre> *
         *
         * @param menuConsumer callback, that will be invoked after inflating.
         */
        fun mapMenu(menuConsumer: MenuConsumer?): Builder {
            this.menuConsumer = menuConsumer
            return this
        }

        /**
         * @return created [BottomSheetMenu] object
         */
        fun build(): BottomSheetMenu {
            return BottomSheetMenu(this)
        }

        /**
         * Creates and shows [BottomSheetMenu].
         * @see BottomSheetMenu.show
         */
        fun show() {
            build().show()
        }
    }

    /**
     * Interface responsible for receiving menu item click events.
     */
    interface OnBottomMenuListener {

        /**
         * This method will be invoked when a menu item is clicked.
         *
         * @param item the menu item that was clicked
         */

        fun onMenuItemSelected(item: MenuItem)
    }

    /**
     * Interface responsible for receiving and consuming menu after inflating.
     */
    interface MenuConsumer {

        /**
         * This method will be invoked after inflating the menu resource file.
         *
         * @param menu menu item, which was inflated from Xml resource
         */
        fun accept(menu: Menu)
    }

    companion object {
        const val LIST = 34
        const val GRID = 35
    }
}
