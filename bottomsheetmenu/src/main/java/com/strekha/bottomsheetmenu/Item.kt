package com.strekha.bottomsheetmenu

import android.support.v4.internal.view.SupportMenuItem

internal sealed class Item {

    abstract fun getWeight(maxWeight: Int): Int

    class Title(val title: CharSequence) : Item() {

        override fun getWeight(maxWeight: Int): Int {
            return maxWeight
        }
    }

    class Element(val item: SupportMenuItem) : Item() {

        override fun getWeight(maxWeight: Int): Int {
            return 1
        }
    }

    object Separator : Item() {

        override fun getWeight(maxWeight: Int): Int {
            return maxWeight
        }
    }
}
