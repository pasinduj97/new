package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class AdminFragment extends Fragment {

    private TextView textView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText txt;
    private Button update;
    private ImageView del;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_admin_profile,container,false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.catautomob);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),ManageSets.class);
                startActivity(intent);
            }
        });




       (view.findViewById(R.id.addcat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCategory();
            }
        });

        (view.findViewById(R.id.delautomob)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delCategory();
            }
        });

        (view.findViewById(R.id.editautomb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCategory();
            }
        });



    }

    public void newCategory(){

        dialogBuilder = new AlertDialog.Builder(getActivity());

        final View catpopup = getLayoutInflater().inflate(R.layout.catpopup,null);

        dialogBuilder.setView(catpopup);
        dialog = dialogBuilder.create();
        dialog.show();


        ImageButton xpop = catpopup.findViewById(R.id.xcatpopup);

        xpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void delCategory(){

        dialogBuilder = new AlertDialog.Builder(getActivity());

        final View dispopup = getLayoutInflater().inflate(R.layout.activity_discard_popup,null);

        dialogBuilder.setView(dispopup);
        dialog = dialogBuilder.create();
        dialog.show();


        Button xpop = dispopup.findViewById(R.id.button2);

        xpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void editCategory(){

        dialogBuilder = new AlertDialog.Builder(getActivity());

        final View editpopup = getLayoutInflater().inflate(R.layout.editcatpopup,null);

        dialogBuilder.setView(editpopup);
        dialog = dialogBuilder.create();
        dialog.show();


        Button xpop = editpopup.findViewById(R.id.button);

        xpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}
