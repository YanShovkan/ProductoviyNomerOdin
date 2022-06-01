package com.example.productoviynomerodin.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;
import com.example.productoviynomerodin.database.logic.UserLogic;

public class ProfileActivity extends AppCompatActivity {
    Button button_change_password;
    Button button_delete_profile;
    Button button_user_card;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getExtras().getString("userId");
        String userLogin = getIntent().getExtras().getString("userLogin");

        this.setTitle("Профиль: " + userLogin);

        button_change_password = findViewById(R.id.button_change_password);
        button_delete_profile = findViewById(R.id.button_delete_profile);
        button_user_card = findViewById(R.id.button_user_card);
        editTextPassword = findViewById(R.id.edit_text_password);

        UserLogic logic = new UserLogic();

        button_change_password.setOnClickListener(
                v -> {
                    if (editTextPassword.getText().toString().length() < 4) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Пароль должен состоять из более чем 3 символов!");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "ОК",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert = builder.create();
                        alert.show();
                        return;
                    }

                    logic.updatePassword(userId, String.valueOf(logic.hash(editTextPassword.getText().toString())));
                }
        );

        button_delete_profile.setOnClickListener(
                v -> {
                    logic.deleteUser(userId);
                    Intent intent = new Intent(ProfileActivity.this, EnterActivity.class);
                    startActivity(intent);
                }
        );

        button_user_card.setOnClickListener(
                v -> {
                    Intent intent = new Intent(ProfileActivity.this, CardActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
        );

    }
}