package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;
import java.util.Map;

import static com.example.madproject.UserDashboard.selected_cat_index;

public class CatsAdapter extends BaseAdapter {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Context con;
    private String fname,email;
    private List<CatModel> catList;
    private List<Integer> catimgList;

    public CatsAdapter(List<CatModel> catList, List<Integer> catimgList) {
        this.catList = catList;
        this.catimgList = catimgList;
    }

    public void addContext(Context context){

        con = context;

    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;

        if (convertView == null){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);


        }

        else {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserDashboard.selected_cat_index = position;

                Intent intent = new Intent(parent.getContext(),SetsActivity.class);

                parent.getContext().startActivity(intent);




            }
        });

        ImageView x = view.findViewById(R.id.x);

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delQuiz(position);

            }
        });

        ((TextView) view.findViewById(R.id.categoryad)).setText(catList.get(position).getName());
        ((ImageView) view.findViewById(R.id.catimg)).setImageResource(catimgList.get(position));





        return view;
    }

    public void delQuiz(final int position){


        dialogBuilder = new AlertDialog.Builder(con);
        final View quizpopup = LayoutInflater.from(con).inflate(R.layout.activity_discard_popup, null);
        dialogBuilder.setView(quizpopup);
        dialog = dialogBuilder.create();
        dialog.show();

        Button no = quizpopup.findViewById(R.id.no);
        Button yes = quizpopup.findViewById(R.id.yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCategory(position);

            }
        });



    }

    private void deleteCategory(final int id)
    {


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


        Map<String,Object> catDoc = new ArrayMap<>();
        int index=1;
        for(int i=0; i < catList.size(); i++)
        {
            if( i != id)
            {
                catDoc.put("CAT" + String.valueOf(index) + "_ID", catList.get(i).getIds());
                catDoc.put("CAT" + String.valueOf(index) + "_NAME", catList.get(i).getName());
                index++;
            }

        }



        catDoc.put("COUNT", index - 1);


        firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(catDoc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(con,"Category deleted successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(con,UserDashboard.class);
                        con.startActivity(intent);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(con,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


    }

}
