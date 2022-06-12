package com.example.productoviynomerodin.database.models;

public class MovementOfGoodModel {
    public String id;
    public String date;
    public String type;

    public MovementOfGoodModel(String date, String type){
        this.date = date;
        this.type = type;
    }

    public MovementOfGoodModel(){

    }
}
