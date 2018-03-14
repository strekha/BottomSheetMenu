package com.strekha.bottomsheetmenu;

import android.support.annotation.NonNull;
import android.view.MenuItem;

class Item {

    private Item() {
    }

    static class Title extends Item {

        @NonNull
        final CharSequence title;

        public Title(@NonNull CharSequence title) {
            this.title = title;
        }
    }

    static class Element extends Item {

        @NonNull
        final MenuItem item;

        public Element(@NonNull MenuItem item) {
            this.item = item;
        }
    }

    static class Separator extends Item {

    }
}
