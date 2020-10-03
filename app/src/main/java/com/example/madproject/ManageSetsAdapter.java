package com.example.madproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Map;

import static com.example.madproject.ManageCategories.manage_cat_list;
import static com.example.madproject.ManageCategories.selected_cat_index;
import static com.example.madproject.ManageSetsAdmin.selected_set_index;

public class ManageSetsAdapter extends RecyclerView.Adapter<ManageSetsAdapter.ViewHolder>{
    private List<String> setIDs;

    public ManageSetsAdapter(List<String> setIDs) {
        this.setIDs = setIDs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup ViewGroup, int viewType) {
        View view = LayoutInflater.from(ViewGroup.getContext()).inflate(R.layout.activity_manage_sets_admin_item,ViewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String setID = setIDs.get(position);
        viewHolder.setData(position, setID, this);
    }

    @Override
    public int getItemCount() {
        return setIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView setName;
        private ImageView deleteSetB;
        private Dialog loadingDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            setName = itemView.findViewById(R.id.setName);
            deleteSetB = itemView.findViewById(R.id.setDelB);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.progress_bar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void setData(final int position, final String setID, final ManageSetsAdapter adapter){
            setName.setText("SET "+ String.valueOf(position+1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selected_set_index = position;

                    Intent intent = new Intent(itemView.getContext(), ManageQuestion.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            deleteSetB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog dialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Set")
                            .setMessage("Do you want to delete this set ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteSet(position, setID,itemView.getContext(), adapter);
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                    dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,50,0);
                    dialog.getButton(dialog.BUTTON_NEGATIVE).setLayoutParams(params);


                }
            });

        }

        private void deleteSet(final int position, String setID, final Context context, final ManageSetsAdapter adapter)
        {
            loadingDialog.show();

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("quiz").document(manage_cat_list.get(selected_cat_index).getId())
                    .collection(setID).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            WriteBatch batch = firestore.batch();

                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                            {
                                batch.delete(doc.getReference());
                            }

                            batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Map<String, Object> catDoc = new ArrayMap<>();
                                    int index=1;
                                    for(int i=0; i< setIDs.size();  i++)
                                    {
                                        if(i != position)
                                        {
                                            catDoc.put("SET" + String.valueOf(index) + "_ID", setIDs.get(i));
                                            index++;
                                        }
                                    }

                                    catDoc.put("SETS", index-1);

                                    firestore.collection("quiz").document(manage_cat_list.get(selected_cat_index).getId())
                                            .update(catDoc)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context,"Set deleted Sucesfully",Toast.LENGTH_SHORT).show();

                                                    ManageSetsAdmin.setsIDs.remove(position);

                                                    manage_cat_list.get(selected_cat_index).setNoOfSets(String.valueOf(ManageSetsAdmin.setsIDs.size()));

                                                    adapter.notifyDataSetChanged();

                                                    loadingDialog.dismiss();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                    loadingDialog.dismiss();
                                                }
                                            });

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });

        }
    }
}
