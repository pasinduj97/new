package com.example.madproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.madproject.R.id.cat_recycler;
import static com.example.madproject.UserDashboard.catList;

public class ManageCategories extends AppCompatActivity {
    private RecyclerView cat_recycler_view;
    private Button addCatB;
    public static List<ManageCategoryModel> manage_cat_list = new ArrayList<>() ;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog, addCatDialog;
    private EditText dialogCatName;
    private Button dialogAddB;
    private ManageCategoriesAdapter adapter;
    private ImageButton ac_close_button;
    public static int selected_cat_index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        Toolbar toolbar = findViewById(R.id.set_toolbar_admin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        addCatB = findViewById(R.id.addCatB);
        cat_recycler_view = findViewById(cat_recycler);

        loadingDialog = new Dialog(ManageCategories.this);
        loadingDialog.setContentView(R.layout.progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        addCatDialog = new Dialog(ManageCategories.this);
        addCatDialog.setContentView(R.layout.catpopup);
        addCatDialog.setCancelable(true);
        addCatDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogCatName = addCatDialog.findViewById(R.id.add_cat_name);
        dialogAddB = addCatDialog.findViewById(R.id.ac_add_button);
        ac_close_button = addCatDialog.findViewById(R.id.ac_close_button);


        firestore = FirebaseFirestore.getInstance();

        addCatB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCatName.getText().clear();
                dialogCatName.setError(null);
                addCatDialog.show();
            }
        });

        dialogAddB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogCatName.getText().toString().isEmpty()){
                    dialogCatName.setError("Enter Category Name");
                    return;
                }
                addNewCategory(dialogCatName.getText().toString());
            }
        });

        ac_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCatName.getText().clear();
                addCatDialog.dismiss();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cat_recycler_view.setLayoutManager(layoutManager);

        loadData();

    }

    private void loadData()
    {
        loadingDialog.show();
        manage_cat_list.clear();

        firestore.collection("quiz").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long count = (long)doc.get("COUNT");

                        for(int i=1; i <= count; i++)
                        {
                            String catName = doc.getString("CAT" + String.valueOf(i) + "_NAME");
                            String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            manage_cat_list.add(new ManageCategoryModel(catID,catName,"0","1"));
                        }

                        adapter = new ManageCategoriesAdapter(manage_cat_list);
                        cat_recycler_view.setAdapter(adapter);


                    }
                    else
                    {
                        Toast.makeText(ManageCategories.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(ManageCategories.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });

    }

    private void addNewCategory(final String title){
        addCatDialog.dismiss();
        loadingDialog.show();

        Map<String,Object> catData = new ArrayMap<>();
        catData.put("NAME",title);
        catData.put("SETS",0);
        catData.put("COUNTER","1");


        final String doc_id = firestore.collection("quiz").document().getId();

        firestore.collection("quiz").document(doc_id)
                .set(catData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String,Object> catDoc = new ArrayMap<>();
                        catDoc.put("CAT" + String.valueOf(manage_cat_list.size() + 1) + "_NAME",title);
                        catDoc.put("CAT" + String.valueOf(manage_cat_list.size() + 1) + "_ID",doc_id);
                        catDoc.put("COUNT", manage_cat_list.size() + 1);

                        firestore.collection("quiz").document("Categories")
                                .update(catDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(ManageCategories.this,"Category added successfully",Toast.LENGTH_SHORT).show();

                                        manage_cat_list.add(new ManageCategoryModel(doc_id,title,"0","1"));

                                        adapter.notifyItemInserted(manage_cat_list.size());

                                        loadingDialog.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ManageCategories.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageCategories.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, adminHome.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}