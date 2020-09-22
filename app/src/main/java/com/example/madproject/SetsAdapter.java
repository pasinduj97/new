package com.example.madproject;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetsAdapter extends BaseAdapter{

    private int numOfSets;

    public SetsAdapter(int numOfSets) {
        this.numOfSets = numOfSets;
    }



    @Override
    public int getCount() {
        return numOfSets;
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

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item_layout,parent,false);


        }

        else {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.setNo_tv)).setText(String.valueOf(position+1));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(),QuestionActivity.class);
                intent.putExtra("setNo",position+1);
                parent.getContext().startActivity(intent);

            }
        });



        return view;
    }
}