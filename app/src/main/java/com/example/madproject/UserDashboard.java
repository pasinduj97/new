package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDashboard extends AppCompatActivity{

    private DrawerLayout drawer;
    public static List<CatModel> catList = new ArrayList<>();
    public static List<Integer> catimgList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseFirestore fire;
    public static int selected_cat_index = 0;
    private Dialog loader;
    private TextView logout,fullname;
    private FirebaseAuth fAuth;
    private String userId;
    private GridView category;
    private ImageView addNewCat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);


        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        loader = new Dialog(UserDashboard.this);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);
        loader.show();



        category = findViewById(R.id.cat);


        catimgList.add(R.drawable.politics);
        catimgList.add(R.drawable.law);
        catimgList.add(R.drawable.sports);
        catimgList.add(R.drawable.sports);
        catimgList.add(R.drawable.sports);
        catimgList.add(R.drawable.sports);




        logout = findViewById(R.id.logt);
        addNewCat = findViewById(R.id.addNewCat);


        firestore = FirebaseFirestore.getInstance();
        fire = FirebaseFirestore.getInstance();

        loadData();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        addNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this,UserAddCat.class);
                startActivity(intent);
            }
        });


        fullname = findViewById(R.id.fullname);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu));


        //fullname.setText("lol");

        DocumentReference documentReference = fire.collection("users").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){

                    fullname.setText(documentSnapshot.getString("fName"));

                    Log.i("username",documentSnapshot.getString("fName"));

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });



    }


    private void loadData(){

        catList.clear();

        firestore.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if(documentSnapshot.exists()){

                    long count = (long)documentSnapshot.get("COUNT");

                    for(int i = 1 ;i<=count;i++){

                        String catName = documentSnapshot.getString("CAT" + String.valueOf(i)+ "_NAME");
                        String catId = documentSnapshot.getString("CAT" + String.valueOf(i)+ "_ID");
                        catList.add(new CatModel(catId,catName));
//                    Toast.makeText(UserDashboard.this, cat,Toast.LENGTH_SHORT).show();

                    }



                    CatsAdapter catsAdapter = new CatsAdapter(catList,catimgList);
                    //catsAdapter.addContext(getActivity());
                    catsAdapter.addContext(UserDashboard.this);
                    category.setAdapter(catsAdapter);

                    loader.cancel();

                }

            }
        });



    }




    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),SelectUser.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




}