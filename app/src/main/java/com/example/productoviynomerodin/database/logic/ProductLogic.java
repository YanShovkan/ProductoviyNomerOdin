package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.ProductModel;

import java.util.Map;

public class ProductLogic {
    public ProductModel convertToProduct(String id, Map product){
        ProductModel productModel = new ProductModel();

        productModel.id = id;
        productModel.name  = product.get("name").toString();
        productModel.discount =  Float.parseFloat(product.get("discount").toString());
        productModel.price =  Float.parseFloat(product.get("price").toString());

        return productModel;
    }
}
