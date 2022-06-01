package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.ShopModel;

import java.util.Map;

public class ShopLogic {
    public ShopModel convertToShop(String id, Map shop){
        ShopModel shopModel = new ShopModel();

        shopModel.id = id;
        shopModel.latitude  = Float.parseFloat(shop.get("latitude").toString());
        shopModel.longitude =  Float.parseFloat(shop.get("longitude").toString());
        shopModel.adress =  shop.get("adress").toString();

        return shopModel;
    }
}
