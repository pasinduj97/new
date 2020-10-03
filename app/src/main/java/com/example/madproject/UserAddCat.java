package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserAddCat extends AppCompatActivity {

    private FirebaseFirestore firestore;
    public static List<AddCatModel> AddCatList = new ArrayList<>();
    private RecyclerView catrecycler;
    private Dialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add_category);


        loader = new Dialog(UserAddCat.this);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);
        loader.show();

        catrecycler = findViewById(R.id.catrecycler);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        catrecycler.setLayoutManager(layoutManager);
        firestore = FirebaseFirestore.getInstance();
        loadData();


    }

    private void loadData(){

        AddCatList.clear();

        firestore.collection("quiz").document("Categories").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.i("pos","hey");

                long count = (long)documentSnapshot.get("COUNT");

                for(int i = 1 ;i<=count;i++){

                    String catName = documentSnapshot.getString("CAT" + String.valueOf(i)+ "_NAME");
                    String catId = documentSnapshot.getString("CAT" + String.valueOf(i)+ "_ID");
                    AddCatList.add(new AddCatModel(catId,catName));


                }

                AddCatAdapter addCatAdapter = new AddCatAdapter(AddCatList);
                addCatAdapter.addContext(UserAddCat.this);
                catrecycler.setAdapter(addCatAdapter);


                loader.cancel();




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserAddCat.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                loader.cancel();
            }
        });



    }
}