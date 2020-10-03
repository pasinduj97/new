package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import static com.example.madproject.UserAddCat.AddCatList;
import static com.example.madproject.UserDashboard.catList;

public class AddCatAdapter extends RecyclerView.Adapter<AddCatAdapter.ViewHolder>{

    private List<AddCatModel> AddCatList;
    private Context con;



    public AddCatAdapter(List<AddCatModel> catList) {
        this.AddCatList = catList;

    }

    public void addContext(Context context){

        con = context;

    }

    @NonNull
    @Override
    public AddCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_category_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCatAdapter.ViewHolder holder, int position) {

        String catname = AddCatList.get(position).getName();
        holder.setData(catname,position,this,con);

    }

    @Override
    public int getItemCount() {
        return AddCatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.usercatname);

        }

        public void setData(String catname, final int position, final AddCatAdapter addCatAdapter, final Context context) {

            categoryName.setText(catname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String categoryName = AddCatList.get(position).getName();
                    String categoryId = AddCatList.get(position).getIds();

                    addNewCategory(categoryName,categoryId,context);


                }
            });

        }

        public void addNewCategory(final String name, String id, final Context context){


            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            Map<String,Object> catDoc = new ArrayMap<>();
            catDoc.put("CAT" + String.valueOf(catList.size() + 1) + "_NAME",name);
            catDoc.put("CAT" + String.valueOf(catList.size() + 1) + "_ID",id);
            catDoc.put("COUNT", catList.size() + 1);

            firebaseFirestore.collection("users").document(userid).update(catDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context,"Category added!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,UserDashboard.class);
                    context.startActivity(intent);

                }
            });




        }



    }
}
