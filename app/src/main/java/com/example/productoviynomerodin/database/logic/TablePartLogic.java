package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.TablePartModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class TablePartLogic {
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void addTablePart(TablePartModel tablePartModel){
        db.child("TableParts").push().setValue(tablePartModel);
    }

    public TablePartModel convertToTablePart(String id, Map tablePart){
        TablePartModel tablePartModel = new TablePartModel();

        tablePartModel.id = id;
        tablePartModel.productId  = tablePart.get("productId").toString();
        tablePartModel.movmentOfGoodId =  tablePart.get("movmentOfGoodId").toString();
        tablePartModel.count = Integer.parseInt(tablePart.get("count").toString());

        return tablePartModel;
    }
}
