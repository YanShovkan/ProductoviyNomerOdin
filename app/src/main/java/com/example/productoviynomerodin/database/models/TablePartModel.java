package com.example.productoviynomerodin.database.models;

public class TablePartModel {
    public String id;
    public String productId;
    public String movmentOfGoodId;
    public int count;

    public TablePartModel(String productId, String movmentOfGoodId, int count){
        this.productId = productId;
        this.movmentOfGoodId = movmentOfGoodId;
        this.count = count;
    }

    public TablePartModel(){

    }
}
