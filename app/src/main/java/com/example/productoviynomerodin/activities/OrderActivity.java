package com.example.productoviynomerodin.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;
import com.example.productoviynomerodin.database.logic.BasketLogic;
import com.example.productoviynomerodin.database.logic.BasketProductLogic;
import com.example.productoviynomerodin.database.logic.CardLogic;
import com.example.productoviynomerodin.database.logic.ProductLogic;
import com.example.productoviynomerodin.database.models.BasketModel;
import com.example.productoviynomerodin.database.models.BasketProductModel;
import com.example.productoviynomerodin.database.models.CardModel;
import com.example.productoviynomerodin.database.models.ProductModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    TableRow selectedRow;

    BasketLogic basketLogic;
    BasketProductLogic basketProductLogic;
    ProductLogic productLogic;
    CardLogic cardLogic;

    Button button_add_order;
    Button button_delete_product;
    Button button_add_product;
    EditText edit_text_count;
    TextView text_view_total_price;

    List<BasketProductModel> basketProducts;
    List<ProductModel> products;
    List<BasketModel> baskets;

    CardModel card;
    float cardDiscount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        String id = getIntent().getExtras().getString("id");
        String userId = getIntent().getExtras().getString("userId");

        text_view_total_price = findViewById(R.id.text_view_total_price);
        edit_text_count = findViewById(R.id.edit_text_count);
        button_add_order = findViewById(R.id.button_add_order);
        button_add_product = findViewById(R.id.button_add_product);
        button_delete_product = findViewById(R.id.button_delete_product);
        Spinner spinnerProducts = findViewById(R.id.spinner_products);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        basketLogic = new BasketLogic();
        basketProductLogic = new BasketProductLogic();
        productLogic = new ProductLogic();
        cardLogic = new CardLogic();

        basketLogic.addBasket(new BasketModel(System.currentTimeMillis(), 0, userId));

        products = new LinkedList<>();
        basketProducts = new LinkedList<>();

        cardDiscount = 0;

        ref.child("Cards").get().addOnCompleteListener(task -> {
            if (((Map<String, Object>) task.getResult().getValue()) != null) {
                List<CardModel> cards = new LinkedList<>();
                for (Map.Entry<String, Object> card : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                    cards.add(cardLogic.convertToCard(card.getKey(), (Map) card.getValue()));
                }

                for (CardModel card : cards) {
                    if (card.userId.equals(userId)) {
                        cardDiscount = card.discount;
                        this.card = card;
                    }
                }
            }
        });

        ref.child("Products").get().addOnCompleteListener(task -> {
            if (((Map<String, Object>) task.getResult().getValue()) != null) {


                for (Map.Entry<String, Object> product : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                    products.add(productLogic.convertToProduct(product.getKey(), (Map) product.getValue()));
                }

                List<String> productNames = new LinkedList<>();

                for (ProductModel product : products) {
                    productNames.add(product.name);
                }

                ArrayAdapter<String> adapterProducts = new ArrayAdapter(this, android.R.layout.simple_spinner_item, productNames);
                adapterProducts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProducts.setAdapter(adapterProducts);
            }
        });


        button_add_product.setOnClickListener(
                v -> {
                    if (edit_text_count.getText().toString().isEmpty()) {
                        return;
                    }
                    String productId = products.get(spinnerProducts.getSelectedItemPosition()).id;
                    for (BasketProductModel basketProduct : basketProducts) {
                        if (basketProduct.productId.equals(productId)) {
                            basketProducts.remove(basketProduct);
                        }
                    }
                    baskets = new LinkedList<>();

                    ref.child("Baskets").get().addOnCompleteListener(task -> {
                        if (((Map<String, Object>) task.getResult().getValue()) != null) {
                            for (Map.Entry<String, Object> basket : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                                baskets.add(basketLogic.convertToBasket(basket.getKey(), (Map) basket.getValue()));
                            }

                            BasketProductModel basketProduct;
                            if (id.equals("")) {
                                basketProduct = new BasketProductModel(baskets.get(baskets.size() - 1).id, productId, Integer.valueOf(edit_text_count.getText().toString()));
                            } else {
                                basketProduct = new BasketProductModel(id, productId, Integer.valueOf(edit_text_count.getText().toString()));
                            }
                            basketProducts.add(basketProduct);
                            edit_text_count.setText("");
                            spinnerProducts.setSelection(0);
                            fillTable(Arrays.asList("Название", "Количество", "Стоимость"), basketProducts, products);

                            float totalPrice = calculateTotalPrice();

                            text_view_total_price.setText("Итог: " + totalPrice);
                        }
                    });


                }
        );

        button_delete_product.setOnClickListener(
                v -> {
                    if (selectedRow != null) {
                        TextView textView = (TextView) selectedRow.getChildAt(3);
                        int index = Integer.valueOf(textView.getText().toString());
                        basketProducts.remove(index);
                        fillTable(Arrays.asList("Название", "Количество", "Стоимость"), basketProducts, products);

                        float totalPrice = 0;
                        for (BasketProductModel currentProduct : basketProducts) {
                            ProductModel product = new ProductModel();
                            for (ProductModel searchProduct : products) {
                                if (searchProduct.id.equals(currentProduct.productId)) {
                                    product = searchProduct;
                                }
                            }
                            totalPrice += currentProduct.count * product.price * (100 - product.discount) / 100;
                        }
                        text_view_total_price.setText("Итог: " + totalPrice);
                    }

                }
        );

        button_add_order.setOnClickListener(
                v -> {
                    if (id.equals("")) {
                        basketLogic.updatePrice(baskets.get(baskets.size() - 1).id, calculateTotalPrice());

                    } else {
                        basketLogic.updatePrice(id, calculateTotalPrice());
                    }

                    if (card != null) {
                        float totalSpent = 0;
                        for (BasketModel basket : baskets) {
                            if (basket.userId.equals(userId)) {
                                totalSpent += basket.totalPrice;
                            }
                        }
                        float discount = 1.5f;
                        if (totalSpent > 5000) {
                            discount = 3f;
                        }
                        if (totalSpent > 10000) {
                            discount = 5f;
                        }
                        if (totalSpent > 50000) {
                            discount = 10f;
                        }
                        if (totalSpent > 100000) {
                            discount = 20f;
                        }

                        cardLogic.updateDiscount(card.id, discount);
                    }

                    List<BasketProductModel> allBasketProducts = new LinkedList<>();
                    ref.child("BasketProducts").get().addOnCompleteListener(task -> {
                        if (((Map<String, Object>) task.getResult().getValue()) != null) {
                            for (Map.Entry<String, Object> basketProduct : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                                allBasketProducts.add(basketProductLogic.convertToBasketProduct(basketProduct.getKey(), (Map) basketProduct.getValue()));
                            }
                            for (BasketProductModel basketProduct : allBasketProducts) {

                                if (id.equals("")) {
                                    if (basketProduct.basketId.equals(baskets.get(baskets.size() - 1).id)) {
                                        basketProductLogic.deleteBasketProduct(basketProduct.id);
                                    }
                                } else {
                                    if (basketProduct.basketId.equals(id)) {
                                        basketProductLogic.deleteBasketProduct(basketProduct.id);
                                    }
                                }
                            }
                        }

                        for (BasketProductModel basketProduct : basketProducts) {
                            basketProductLogic.addBasketProduct(basketProduct);
                        }
                    });
                });
    }

    float calculateTotalPrice() {
        float totalPrice = 0;
        for (BasketProductModel basketProduct : basketProducts) {
            ProductModel product = new ProductModel();
            for (ProductModel searchProduct : products) {
                if (searchProduct.id.equals(basketProduct.productId)) {
                    product = searchProduct;
                }
            }
            totalPrice += basketProduct.count * product.price * (100 - product.discount) / 100;
        }
        return totalPrice * (100 - cardDiscount) / 100;
    }

    void fillTable(List<String> titles, List<BasketProductModel> basketProducts, List<ProductModel> products) {

        TableLayout tableLayoutBasketProducts = findViewById(R.id.tableLayoutBasketProducts);

        tableLayoutBasketProducts.removeAllViews();

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
        tableLayoutBasketProducts.addView(tableRowTitles);

        int index = 0;
        for (BasketProductModel basketProduct : basketProducts) {
            TableRow tableRow = new TableRow(this);

            ProductModel product = new ProductModel();

            for (ProductModel searchProduct : products) {
                if (searchProduct.id.equals(basketProduct.productId)) {
                    product = searchProduct;
                }
            }

            TextView textViewName = new TextView(this);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setText(product.name);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewCount = new TextView(this);
            textViewCount.setHeight(100);
            textViewCount.setTextSize(16);
            textViewCount.setText(String.valueOf(basketProduct.count));
            textViewCount.setTextColor(Color.WHITE);
            textViewCount.setGravity(Gravity.CENTER);

            TextView textViewPrice = new TextView(this);
            textViewPrice.setHeight(100);
            textViewPrice.setTextSize(16);
            textViewPrice.setText(String.valueOf(basketProduct.count * product.price * (100 - product.discount) / 100));
            textViewPrice.setTextColor(Color.WHITE);
            textViewPrice.setGravity(Gravity.CENTER);

            TextView textViewIndex = new TextView(this);
            textViewIndex.setVisibility(View.INVISIBLE);
            textViewIndex.setText(String.valueOf(index));

            tableRow.addView(textViewName);
            tableRow.addView(textViewCount);
            tableRow.addView(textViewPrice);
            tableRow.addView(textViewIndex);

            tableRow.setBackgroundColor(Color.parseColor("#F43838"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int i = 0; i < tableLayoutBasketProducts.getChildCount(); i++) {
                    View view = tableLayoutBasketProducts.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#F43838"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#7E0202"));
            });

            tableLayoutBasketProducts.addView(tableRow);
            index++;
        }
    }
}