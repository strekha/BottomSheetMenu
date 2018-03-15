package com.strekha.bottomsheetmenu;

import android.support.annotation.NonNull;
import android.view.MenuItem;

abstract class Item {

    abstract int getWeight(int maxWeight);

    static class Title extends Item {

        @NonNull
        final CharSequence title;

        Title(@NonNull CharSequence title) {
            this.title = title;
        }

        @Override
        int getWeight(int maxWeight) {
            return maxWeight;
        }
    }

    static class Element extends Item {

        @NonNull
        final MenuItem item;

        Element(@NonNull MenuItem item) {
            this.item = item;
        }

        @Override
        int getWeight(int maxWeight) {
            return 1;
        }
    }

    static class Separator extends Item {

        @Override
        int getWeight(int maxWeight) {
            return maxWeight;
        }
    }
}
