package com.strekha.bottomsheetmenusample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.strekha.bottomsheetmenu.BottomSheetMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text);

        textView.setOnClickListener(v -> showMenu());

    }

    private void showMenu() {
        new BottomSheetMenu.Builder(this)
                .inflate(R.menu.sample)
//                .withType(BottomSheetMenu.GRID)
                .withTitle("Hello")
                .withListener(item -> {
                    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
