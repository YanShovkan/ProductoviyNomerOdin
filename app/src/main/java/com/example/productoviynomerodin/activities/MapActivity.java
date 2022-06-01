package com.example.productoviynomerodin.activities;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;
import com.example.productoviynomerodin.database.logic.ShopLogic;
import com.example.productoviynomerodin.database.models.ShopModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// Implement OnMapReadyCallback.
public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("272f5332-b0fa-4990-81f0-62527a9a64a9");
        MapKitFactory.initialize(this);


        // Укажите имя Activity вместо map.
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.mapview);

        ShopLogic shopLogic = new ShopLogic();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shops");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ShopModel> shops = new LinkedList<>();

                if (((Map<String, Object>) dataSnapshot.getValue()) != null) {
                    for (Map.Entry<String, Object> shop : ((Map<String, Object>) dataSnapshot.getValue()).entrySet()) {
                        shops.add(shopLogic.convertToShop(shop.getKey(), (Map) shop.getValue()));
                    }

                    for(ShopModel shop : shops){
                        mapView.getMap().getMapObjects().addPlacemark(new Point(shop.longitude, shop.latitude));
                    }

                    mapView.getMap().move(
                            new CameraPosition(new Point(shops.get(0).longitude, shops.get(0).latitude), 20.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 3),
                            null);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                builder.setMessage("Произошла ошибка с кодом: " + databaseError.getCode());
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "ОК",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();
                return;
            }
        });

    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}