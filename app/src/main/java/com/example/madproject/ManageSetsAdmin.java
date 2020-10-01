package com.example.madproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.madproject.ManageCategories.manage_cat_list;
import static com.example.madproject.ManageCategories.selected_cat_index;

public class ManageSetsAdmin extends AppCompatActivity {

    private RecyclerView setsView;
    private Button addSetB;
    public static List<String> setsIDs = new ArrayList<>();
    public static int selected_set_index=0;
    private ManageSetsAdapter adapter;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sets_admin );

        Toolbar toolbar = findViewById(R.id.set_toolbar_admin_sets);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setsView = findViewById(R.id.sets_recycler);
        addSetB = findViewById(R.id.addSetsB);

        loadingDialog = new Dialog(ManageSetsAdmin.this);
        loadingDialog.setContentView(R.layout.progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addSetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSet();
            }
        });

        firestore= FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setsView.setLayoutManager(layoutManager);

        loadSets();
    }

    private void loadSets(){

        setsIDs.clear();

        loadingDialog.show();

        firestore.collection("quiz").document(manage_cat_list.get(selected_cat_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long noOfSets = (long) documentSnapshot.get("SETS");

                for(int i = 1; i <= noOfSets; i++){
                    setsIDs.add(documentSnapshot.getString("SET"+ String.valueOf(i) + "_ID"));
                }

                manage_cat_list.get(selected_cat_index).setSetCounter(documentSnapshot.getString("COUNTER"));
                manage_cat_list.get(selected_cat_index).setNoOfSets(String.valueOf(noOfSets));

                adapter = new ManageSetsAdapter(setsIDs);
                setsView.setAdapter(adapter);

                loadingDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ManageSetsAdmin.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addNewSet()
    {
        loadingDialog.show();

        final String curr_cat_id = manage_cat_list.get(selected_cat_index).getId();
        final String curr_counter = manage_cat_list.get(selected_cat_index).getSetCounter();

        Map<String,Object> qData = new ArrayMap<>();
        qData.put("COUNT","0");

        firestore.collection("quiz").document(curr_cat_id)
                .collection(curr_counter).document("QUESTIONS_LIST")
                .set(qData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String,Object> catDoc = new ArrayMap<>();
                        catDoc.put("COUNTER", String.valueOf(Integer.valueOf(curr_counter) + 1)  );
                        catDoc.put("SET" + String.valueOf(setsIDs.size() + 1) + "_ID", curr_counter);
                        catDoc.put("SETS", setsIDs.size() + 1);

                        firestore.collection("quiz").document(curr_cat_id)
                                .update(catDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(ManageSetsAdmin.this, " Set Added Successfully",Toast.LENGTH_SHORT).show();

                                        setsIDs.add(curr_counter);
                                        manage_cat_list.get(selected_cat_index).setNoOfSets(String.valueOf(setsIDs.size()));
                                        manage_cat_list.get(selected_cat_index).setSetCounter(String.valueOf(Integer.valueOf(curr_counter) + 1));

                                        adapter.notifyItemInserted(setsIDs.size());
                                        loadingDialog.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ManageSetsAdmin.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageSetsAdmin.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, ManageCategories.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
