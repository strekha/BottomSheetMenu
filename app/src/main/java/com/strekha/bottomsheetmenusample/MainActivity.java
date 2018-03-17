package com.strekha.bottomsheetmenusample;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.strekha.bottomsheetmenu.BottomSheetMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.list).setOnClickListener(v -> showMenu(BottomSheetMenu.LIST));
        findViewById(R.id.grid).setOnClickListener(v -> showMenu(BottomSheetMenu.GRID));

    }

    private void showMenu(int type) {
        new BottomSheetMenu.Builder(this)
                .inflate(R.menu.sample)
                .withType(type)
                .withTitle("Hello")
                .withListener(item -> {
                    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
