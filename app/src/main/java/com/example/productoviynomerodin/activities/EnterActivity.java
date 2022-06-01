package com.example.productoviynomerodin.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;
import com.example.productoviynomerodin.database.logic.UserLogic;
import com.example.productoviynomerodin.database.models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnterActivity extends AppCompatActivity {

    Button button_to_register_activity;
    Button button_enter;
    EditText editTextLogin;
    EditText editTextPassword;

    UserLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        button_to_register_activity = findViewById(R.id.button_to_register_activity);
        button_enter = findViewById(R.id.button_enter);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);

        logic = new UserLogic();

        button_to_register_activity.setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnterActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
        );

        button_enter.setOnClickListener(v -> {
            UserModel currentUser = new UserModel();

            currentUser.login = editTextLogin.getText().toString();
            currentUser.password = String.valueOf(logic.hash(editTextPassword.getText().toString()));

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

            ref.get().addOnCompleteListener(task -> {
                List<UserModel> users = new LinkedList<>();
                UserModel currentUserFromFB = new UserModel();

                if (((Map<String, Object>) task.getResult().getValue()) == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterActivity.this);
                    builder.setMessage("Такого пользователя не существует, зарегестрируйтесь!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "ОК",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                    return;
                }

                for (Map.Entry<String, Object> user : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                    users.add(logic.convertToUser(user.getKey(), (Map) user.getValue()));
                }

                for (UserModel user : users) {
                    if (user.login.equals(currentUser.login)) {
                        currentUserFromFB = user;
                    }
                }

                if(currentUserFromFB.login == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterActivity.this);
                    builder.setMessage("Такого пользователя не существует, зарегестрируйтесь!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "ОК",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                    return;
                }

                if (currentUserFromFB.password.equals(currentUser.password)) {
                    Intent intent = new Intent(EnterActivity.this, MainActivity.class);
                    intent.putExtra("userId", currentUserFromFB.id);
                    intent.putExtra("userLogin", currentUserFromFB.login);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterActivity.this);
                    builder.setMessage("Пароль введен неверно!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "ОК",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        });
    }
}