package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.CardModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class CardLogic {
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void addCard(CardModel card){
        db.child("Cards").push().setValue(card);
    }

    public void updateDiscount(String id, float discount){
        db.child("Cards").child(id).child("discount").setValue(discount);
    }

    public CardModel convertToCard(String id, Map basket){
        CardModel cardModel = new CardModel();

        cardModel.id = id;
        cardModel.discount =  Float.parseFloat(basket.get("discount").toString());
        cardModel.userId =  basket.get("userId").toString();

        return cardModel;
    }
}
