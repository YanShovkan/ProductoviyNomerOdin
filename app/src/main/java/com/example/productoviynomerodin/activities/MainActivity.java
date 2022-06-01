package com.example.productoviynomerodin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;

public class MainActivity extends AppCompatActivity {

    Button button_map;
    Button button_catalog;
    Button button_profile;
    Button button_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String userId = getIntent().getExtras().getString("userId");
        String userLogin = getIntent().getExtras().getString("userLogin");
        button_map = findViewById(R.id.button_map);
        button_catalog = findViewById(R.id.button_catalog);
        button_profile = findViewById(R.id.button_profile);
        button_order = findViewById(R.id.button_order);

        button_map.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                }
        );

        button_catalog.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                    startActivity(intent);
                }
        );

        button_profile.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userLogin", userLogin);
                    startActivity(intent);
                }
        );

        button_order.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("id", "");
                    startActivity(intent);
                }
        );
    }
}