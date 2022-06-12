package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.MovementOfGoodModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MovementOfGoodLogic {
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void addMovementOfGood(MovementOfGoodModel movementOfGoodModel){
        db.child("MovementOfGoods").push().setValue(movementOfGoodModel);
    }

    public MovementOfGoodModel convertToMovementOfGood(String id, Map movementOfGood){
        MovementOfGoodModel movementOfGoodModel = new MovementOfGoodModel();

        movementOfGoodModel.id = id;
        movementOfGoodModel.date  = movementOfGood.get("date").toString();
        movementOfGoodModel.type =  movementOfGood.get("type").toString();

        return movementOfGoodModel;
    }
}
