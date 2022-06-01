package com.example.productoviynomerodin.database.models;

public class UserModel {
    public String id;
    public String login;
    public String password;
    public String status;

    public UserModel(String login,  String password, String status){
        this.login = login;
        this.password = password;
        this.status = status;
    }

    public UserModel(){

    }
}
