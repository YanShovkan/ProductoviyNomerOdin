package com.example.productoviynomerodin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productoviynomerodin.R;
import com.example.productoviynomerodin.database.logic.CardLogic;
import com.example.productoviynomerodin.database.models.CardModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CardActivity extends AppCompatActivity {
    Button button_create_card;
    TextView text_view_discount;
    CardLogic cardLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        cardLogic = new CardLogic();

        String userId = getIntent().getExtras().getString("userId");
        button_create_card = findViewById(R.id.button_create_card);
        text_view_discount = findViewById(R.id.text_view_discount);

        List<CardModel> cards = new LinkedList<>();

        ref.child("Cards").get().addOnCompleteListener(task -> {
            if (((Map<String, Object>) task.getResult().getValue()) != null) {
                for (Map.Entry<String, Object> card : ((Map<String, Object>) task.getResult().getValue()).entrySet()) {
                    cards.add(cardLogic.convertToCard(card.getKey(), (Map) card.getValue()));
                }
                for (CardModel card : cards) {
                    if (card.userId.equals(userId)) {
                        text_view_discount.setText("Скидка по карте: " + card.discount + "%");
                        button_create_card.setVisibility(View.INVISIBLE);
                        button_create_card.setEnabled(false);
                    }
                }
            }
        });

        button_create_card.setOnClickListener(v -> {
            cardLogic.addCard(new CardModel(1.5f, userId));
            button_create_card.setVisibility(View.INVISIBLE);
            button_create_card.setEnabled(false);
        });
    }
}