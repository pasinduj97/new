package com.example.madproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.madproject.ManageCategories.manage_cat_list;
import static com.example.madproject.ManageCategories.selected_cat_index;
import static com.example.madproject.ManageSetsAdmin.selected_set_index;
import static com.example.madproject.ManageSetsAdmin.setsIDs;

public class ManageQuestion extends AppCompatActivity {

    private RecyclerView quesView;
    private Button addnewQB;
    public static List<AdQuestionModels> quesList = new ArrayList<>();
    private AdQuestionAdapter adapter;
    private FirebaseFirestore firestore;
    private Dialog loadingDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_question);

        Toolbar toolbar = findViewById(R.id.set_toolbar_admin_manage_question);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quesView = findViewById(R.id.question_recycler);
        addnewQB = findViewById(R.id.addQuestionB);

        loadingDailog = new Dialog(ManageQuestion.this);
        loadingDailog.setContentView(R.layout.progress_bar);
        loadingDailog.setCancelable(false);
        loadingDailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addnewQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ManageQuestion.this, AdQuestionDetailsActivity.class);
                intent.putExtra("ACTION","ADD");
                startActivity(intent);

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        quesView.setLayoutManager(layoutManager);

        firestore = FirebaseFirestore.getInstance();

        loadQuestions();
    }

    private void loadQuestions()
    {
        quesList.clear();

        loadingDailog.show();

        firestore.collection("quiz").document(manage_cat_list.get(selected_cat_index).getId())
                .collection(setsIDs.get(selected_set_index)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }

                        QueryDocumentSnapshot quesListDoc  = docList.get("QUESTIONS_LIST");

                        String count = quesListDoc.getString("COUNT");

                        for(int i=0; i < Integer.valueOf(count); i++)
                        {
                            String quesID = quesListDoc.getString("Q" + String.valueOf(i+1) + "_ID");
                            Log.i("quesID",quesID);

                            QueryDocumentSnapshot quesDoc = docList.get(quesID);

                            quesList.add(new AdQuestionModels(
                                    quesID,
                                    quesDoc.getString("QUESTION"),
                                    quesDoc.getString("A"),
                                    quesDoc.getString("B"),
                                    quesDoc.getString("C"),
                                    quesDoc.getString("D"),
                                    Integer.valueOf(quesDoc.getString("ANSWER"))
                            ));

                            Log.i("add","add");

                        }

                        adapter = new AdQuestionAdapter(quesList);
                        quesView.setAdapter(adapter);

                        loadingDailog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageQuestion.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDailog.dismiss();
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}