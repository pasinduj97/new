package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import static com.example.madproject.UserDashboard.selected_cat_index;

public class CatsAdapter extends BaseAdapter {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Context con;

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

                delQuiz();

            }
        });

        ((TextView) view.findViewById(R.id.categoryad)).setText(catList.get(position).getName());
        ((ImageView) view.findViewById(R.id.catimg)).setImageResource(catimgList.get(position));





        return view;
    }

    public void delQuiz(){


        dialogBuilder = new AlertDialog.Builder(con);
        final View quizpopup = LayoutInflater.from(con).inflate(R.layout.activity_discard_popup, null);
        dialogBuilder.setView(quizpopup);
        dialog = dialogBuilder.create();
        dialog.show();

        Button xpop = quizpopup.findViewById(R.id.button);

        xpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

}
