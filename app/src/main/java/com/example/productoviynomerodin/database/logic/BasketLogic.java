package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.BasketModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class BasketLogic {
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void addBasket(BasketModel basket){
        db.child("Baskets").push().setValue(basket);
    }

    public void updatePrice(String id, float totalPrice){
        db.child("Baskets").child(id).child("totalPrice").setValue(totalPrice);
    }

    public BasketModel convertToBasket(String id, Map basket){
        BasketModel basketModel = new BasketModel();

        basketModel.id = id;
        basketModel.date  = Long.parseLong(basket.get("date").toString());
        basketModel.totalPrice =  Float.parseFloat(basket.get("totalPrice").toString());
        basketModel.userId =  basket.get("userId").toString();
        basketModel.adress = basket.get("adress").toString();

        return basketModel;
    }

    public void deleteBasket(String id){
        db.child("Baskets/" + id).removeValue();
    }
}
