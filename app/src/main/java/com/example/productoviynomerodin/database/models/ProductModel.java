package com.example.productoviynomerodin.database.models;

public class ProductModel {
    public float discount;
    public String id;
    public String name;
    public float price;

    public ProductModel(float discount,  String id, String name,float price){
        this.discount = discount;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductModel(){

    }
}
