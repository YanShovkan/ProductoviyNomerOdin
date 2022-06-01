package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.BasketProductModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class BasketProductLogic {
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void addBasketProduct(BasketProductModel basketProduct){
        db.child("BasketProducts").push().setValue(basketProduct);
    }

    public void deleteBasketProduct(String id){
        db.child("BasketProducts/" + id).removeValue();
    }

    public BasketProductModel convertToBasketProduct(String id,Map basketProduct){
        BasketProductModel basketProductModel = new BasketProductModel();

        basketProductModel.id = id;
        basketProductModel.basketId = basketProduct.get("basketId").toString();
        basketProductModel.productId =  basketProduct.get("productId").toString();
        basketProductModel.count =  Integer.valueOf(basketProduct.get("count").toString());

        return basketProductModel;
    }
}
