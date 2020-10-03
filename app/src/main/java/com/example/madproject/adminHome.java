package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class adminHome extends AppCompatActivity {

    private RelativeLayout adminCatSelect;
    private RelativeLayout adminRefSelect;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminCatSelect = findViewById(R.id.adminCatSelect);
        adminRefSelect = findViewById(R.id.adminRefSelect);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(adminHome.this,"Logged Out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(adminHome.this,SelectUser.class));
            }
        });


        adminCatSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminHome.this,ManageCategories.class);
                startActivity(intent);
                finish();
            }
        });

        adminRefSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminHome.this,ManageReference.class);
                startActivity(intent);
            }
        });

    }
}