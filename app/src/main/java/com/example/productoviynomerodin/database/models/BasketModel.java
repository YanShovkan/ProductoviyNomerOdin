package com.example.productoviynomerodin.database.models;

public class BasketModel {
    public String id;
    public long date;
    public float totalPrice;
    public String userId;
    public String adress;

    public BasketModel(long date,float totalPrice, String userId, String adress){
        this.date = date;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.adress = adress;
    }

    public BasketModel(){

    }
}
