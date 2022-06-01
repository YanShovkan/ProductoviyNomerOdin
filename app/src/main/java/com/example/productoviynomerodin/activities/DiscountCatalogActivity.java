package com.example.productoviynomerodin.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;
import com.example.productoviynomerodin.database.logic.ProductLogic;
import com.example.productoviynomerodin.database.models.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiscountCatalogActivity extends AppCompatActivity {

    TableRow selectedRow;
    ProductLogic productLogic;

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productLogic = new ProductLogic();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_catalog);
        loadData();
    }

    void loadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ProductModel> products = new LinkedList<>();

                if (((Map<String, Object>) dataSnapshot.getValue()) != null) {
                    for (Map.Entry<String, Object> product : ((Map<String, Object>) dataSnapshot.getValue()).entrySet()) {
                        if(productLogic.convertToProduct(product.getKey(), (Map) product.getValue()).discount != 0){
                            products.add(productLogic.convertToProduct(product.getKey(), (Map) product.getValue()));
                        }
                    }
                }

                fillTable(Arrays.asList("Название", "Стоимость, руб.", "Скидка, %"), products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiscountCatalogActivity.this);
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

    void fillTable(List<String> titles, List<ProductModel> products) {

        TableLayout tableLayout = findViewById(R.id.tableLayoutDiscountProducts);

        tableLayout.removeAllViews();

        TableRow tableRowTitles = new TableRow(this);

        for (String title : titles) {
            TextView textView = new TextView(this);

            textView.setTextSize(16);
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth() / 3.2));
            tableRowTitles.addView(textView);
        }

        tableRowTitles.setBackgroundColor(Color.parseColor("#F43838"));
        tableLayout.addView(tableRowTitles);


        for (ProductModel product : products) {
            TableRow tableRow = new TableRow(this);

            TextView textViewName = new TextView(this);
            textViewName.setText(product.name);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewPrice = new TextView(this);
            textViewName.setHeight(100);
            textViewPrice.setTextSize(16);
            textViewPrice.setText(String.valueOf(product.price - product.price * product.discount / 100));
            textViewPrice.setTextColor(Color.WHITE);
            textViewPrice.setGravity(Gravity.CENTER);

            TextView textViewDiscount = new TextView(this);
            textViewName.setHeight(100);
            textViewDiscount.setTextSize(16);
            textViewDiscount.setText(String.valueOf(product.discount));
            textViewDiscount.setTextColor(Color.WHITE);
            textViewDiscount.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(String.valueOf(product.id));

            tableRow.addView(textViewName);
            tableRow.addView(textViewPrice);
            tableRow.addView(textViewDiscount);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#F43838"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int i = 0; i < tableLayout.getChildCount(); i++) {
                    View view = tableLayout.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#F43838"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#7E0202"));
            });

            tableLayout.addView(tableRow);
        }
    }

}