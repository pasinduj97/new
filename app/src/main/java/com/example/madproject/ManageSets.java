package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ManageSets extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set);

        Intent intent = getIntent();

        Toolbar toolbar = findViewById(R.id.set_toolbar_admin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        (findViewById(R.id.addset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSet();
            }
        });



    }

    public void addSet(){

        dialogBuilder = new AlertDialog.Builder(this);

        final View setpopup = getLayoutInflater().inflate(R.layout.setpopup,null);

        dialogBuilder.setView(setpopup);
        dialog = dialogBuilder.create();
        dialog.show();


        Button addpop = setpopup.findViewById(R.id.buttonAddset);

        addpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
