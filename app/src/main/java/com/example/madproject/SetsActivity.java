package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.madproject.UserDashboard.catList;
import static com.example.madproject.UserDashboard.selected_cat_index;

public class SetsActivity extends AppCompatActivity {


    private Toolbar myToolbar;
    private GridView sets_grid;

    private FirebaseFirestore firestore;
    private Dialog loader;
    public static List<String> setsIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Intent intent = getIntent();


        Toolbar toolbar = findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = catList.get(selected_cat_index).getName();

        getSupportActionBar().setTitle(title);

        sets_grid = findViewById(R.id.sets_gridview);

        loader = new Dialog(SetsActivity.this);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);
        loader.show();

        firestore = FirebaseFirestore.getInstance();
        loadData();


    }

    private void loadData() {


        setsIDs.clear();


        firestore.collection("quiz").document(catList.get(selected_cat_index).getIds()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfsets = (long)documentSnapshot.get("SETS");

                for (int i = 1; i <= (int)noOfsets; i++){

                    setsIDs.add(documentSnapshot.getString("SET"+String.valueOf(i)+"_ID"));
                }

                Log.i("pos",String.valueOf(setsIDs.size()));

                SetsAdapter setsAdapter = new SetsAdapter(setsIDs.size());
                sets_grid.setAdapter(setsAdapter);
                loader.cancel();


                //Toast.makeText(SetsActivity.this,selected_cat_index,Toast.LENGTH_SHORT).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i("pos",e.getMessage());
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



