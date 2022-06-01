package com.example.productoviynomerodin.database.models;

public class CardModel {
    public String id;
    public float discount;
    public String userId;

    public CardModel(float discount, String userId){
        this.discount = discount;
        this.userId = userId;
    }

    public CardModel(){

    }
}
