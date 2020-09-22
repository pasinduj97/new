package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {


    private Toolbar myToolbar;
    private GridView sets_grid;
    public  static int categoryId;
    private FirebaseFirestore firestore;
    private Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Intent intent = getIntent();



        Toolbar toolbar = findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("category");
        categoryId = getIntent().getIntExtra("categoryId",1);

        getSupportActionBar().setTitle(title);

        sets_grid = findViewById(R.id.sets_gridview);

        loader = new Dialog(SetsActivity.this);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);
        loader.show();

        firestore = FirebaseFirestore.getInstance();
        loadData();





    }

    private void loadData(){


        firestore.collection("QUIZ").document("CAT"+String.valueOf(categoryId)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfsets = (long)documentSnapshot.get("SETS");

                SetsAdapter setsAdapter = new SetsAdapter((int)noOfsets);
                sets_grid.setAdapter(setsAdapter);
                loader.cancel();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, UserDashboard.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }



}