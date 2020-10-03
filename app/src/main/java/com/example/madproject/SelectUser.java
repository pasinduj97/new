package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectUser extends AppCompatActivity {

    private CardView select_user;
    private CardView select_user2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        select_user = findViewById(R.id.select_user);
        select_user2 = findViewById(R.id.select_user2);

        select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUser.this, Register.class);
                startActivity(intent);
            }
        });

        select_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUser.this, Login.class);
                startActivity(intent);
            }
        });
    }
}