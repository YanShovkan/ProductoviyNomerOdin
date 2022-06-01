package com.example.productoviynomerodin.database.logic;

import com.example.productoviynomerodin.database.models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class UserLogic {
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void addUser(UserModel user){
        db.child("Users").push().setValue(user);
    }

    public void updatePassword(String id, String password){
        db.child("Users").child(id).child("password").setValue(password);
    }

    public void deleteUser(String id){
        db.child("Users/" + id).removeValue();
    }

    public UserModel convertToUser(String id, Map user){
        UserModel userModel = new UserModel();
        userModel.id = id;
        userModel.login = user.get("login").toString();
        userModel.password  = user.get("password").toString();
        userModel.status = user.get("status").toString();

        return userModel;
    }

    public int hash(String password){
        char[] passwd = password.toCharArray();

        int newPassword = 0;

        for(int i = 0; i < passwd.length;i++){
            newPassword += newPassword + passwd[i] * 3 - 10;
        }

        for(int i = 0; i < passwd.length;i++){
            newPassword += newPassword + (passwd[i] - 10) / 3 ;
        }

        return newPassword;
    }

}
