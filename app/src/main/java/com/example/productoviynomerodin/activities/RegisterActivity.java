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

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegister;
    EditText editTextLogin;
    EditText editTextPassword;
    UserLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = findViewById(R.id.button_register);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);

        logic = new UserLogic();

        buttonRegister.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

            ref.get().addOnCompleteListener(task -> {
                UserModel newUser = new UserModel(editTextLogin.getText().toString(), editTextPassword.getText().toString(), "user");
                List<UserModel> users = new LinkedList<>();

                if (newUser.password.length() < 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Пароль должен состоять из более чем 3 символов!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "ОК",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                    return;
                }

                if (((Map<String, Object>) task.getResult().getValue()) != null) {
                    for (Map.Entry<String, Object> user : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                        users.add(logic.convertToUser(user.getKey(), (Map) user.getValue()));
                    }

                    for (UserModel user : users) {
                        if (user.login.equals(newUser.login)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Такой пользователь уже существует!");
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "ОК",
                                    (dialog, id) -> dialog.cancel());

                            AlertDialog alert = builder.create();
                            alert.show();
                            return;
                        }
                    }
                }

                newUser.password = String.valueOf(logic.hash(newUser.password));
                logic.addUser(newUser);
                this.finish();
                Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
                startActivity(intent);
            });
        });

    }
}