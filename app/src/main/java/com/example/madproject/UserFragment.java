package com.example.madproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.madproject.R.layout.catpopup;



public class UserFragment extends Fragment {

    private ImageView imageView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText txt;
    private Button update;
    private ImageView plusv;
    private GridView category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_profile,container,false);



    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        category = view.findViewById(R.id.cat);


        //imageView = view.findViewById(R.id.science);

//        List<String> catList = new ArrayList<>();
//        catList.add("science");
//        catList.add("politics");
//        catList.add("law");
//        catList.add("sports");


        List<Integer> catimgList = new ArrayList<>();

        catimgList.add(R.drawable.science);
        catimgList.add(R.drawable.politics);
        catimgList.add(R.drawable.law);
        catimgList.add(R.drawable.sports);
        catimgList.add(R.drawable.sports);


       List<String> catList = new ArrayList<String>();
       catList = ((UserDashboard) getActivity()).catList;




        CatsAdapter catsAdapter = new CatsAdapter(catList,catimgList);
        catsAdapter.addContext(getActivity());
        category.setAdapter(catsAdapter);


        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),SetsActivity.class);

                startActivity(intent);
            }
        });*/

        /*(view.findViewById(R.id.plusbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCategory();
            }
        });*/





    }

    public void newCategory() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View catpopup = getLayoutInflater().inflate(R.layout.catpopup, null);
        dialogBuilder.setView(catpopup);
        dialog = dialogBuilder.create();
        dialog.show();

       /* ImageButton xpop = catpopup.findViewById(R.id.xcatpopup);

        xpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });*/


    }


}
