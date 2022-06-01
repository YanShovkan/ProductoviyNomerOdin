package com.example.productoviynomerodin.database.models;

public class BasketProductModel {
    public String id;
    public String basketId;
    public String productId;
    public int count;

    public BasketProductModel(String basketId, String productId, int count){
        this.basketId = basketId;
        this.productId = productId;
        this.count = count;
    }

    public BasketProductModel(){

    }
}
