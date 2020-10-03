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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class ManageCategoriesAdapter extends RecyclerView.Adapter<ManageCategoriesAdapter.ViewHolder> {

    private List<ManageCategoryModel> manage_cat_list;


    public ManageCategoriesAdapter(List<ManageCategoryModel> manage_cat_list) {
        this.manage_cat_list = manage_cat_list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup , int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_manage_categories_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String title = manage_cat_list.get(position).getName();
        viewHolder.setData(title,position,this);

    }

    @Override
    public int getItemCount() {
        return manage_cat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView catName;
        private ImageView deleteB;
        private Dialog loadingDialog;
        private Dialog editDialog;
        private EditText tv_editCatName;
        private Button updateCatB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catName = itemView.findViewById(R.id.catName);
            deleteB = itemView.findViewById(R.id.catDelB);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.progress_bar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            editDialog = new Dialog(itemView.getContext());
            editDialog.setContentView(R.layout.editcatpopup);
            editDialog.setCancelable(true);
            editDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            tv_editCatName = editDialog.findViewById(R.id.edit_cat_name);
            updateCatB = editDialog.findViewById(R.id.ac_update_button);

        }


        private void setData(String title, final int position, final ManageCategoriesAdapter adapter)
        {
            catName.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ManageCategories.selected_cat_index = position;

                    Intent intent = new Intent(itemView.getContext(),ManageSetsAdmin.class);

                    itemView.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   tv_editCatName.setText(manage_cat_list.get(position).getName());
                   editDialog.show();

                    return false;
                 }
               });

            updateCatB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(tv_editCatName.getText().toString().isEmpty())
                    {
                        tv_editCatName.setError("Enter Category Name");
                        return;
                    }

                    updateCategory(tv_editCatName.getText().toString(), position, itemView.getContext(), adapter);

                }
            });

            deleteB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog dialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Category")
                            .setMessage("Do you want to delete this category ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteCategory(position, itemView.getContext(), adapter);
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

        private void deleteCategory(final int id, final Context context, final ManageCategoriesAdapter adapter)
        {
            loadingDialog.show();

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();


            Map<String,Object> catDoc = new ArrayMap<>();
            int index=1;
            for(int i=0; i < manage_cat_list.size(); i++)
            {
                if( i != id)
                {
                    catDoc.put("CAT" + String.valueOf(index) + "_ID", manage_cat_list.get(i).getId());
                    catDoc.put("CAT" + String.valueOf(index) + "_NAME", manage_cat_list.get(i).getName());
                    index++;
                }

            }

            catDoc.put("COUNT", index - 1);

            firestore.collection("quiz").document("Categories")
                    .set(catDoc)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(context,"Category deleted successfully",Toast.LENGTH_SHORT).show();

                            ManageCategories.manage_cat_list.remove(id);

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


        private void updateCategory(final String catNewName, final int pos, final Context context, final ManageCategoriesAdapter adapter)
        {
            editDialog.dismiss();

            loadingDialog.show();

            Map<String,Object> catData = new ArrayMap<>();
            catData.put("NAME",catNewName);

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("quiz").document(manage_cat_list.get(pos).getId())
                    .update(catData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Map<String,Object> catDoc = new ArrayMap<>();
                            catDoc.put("CAT" + String.valueOf(pos + 1) + "_NAME",catNewName);

                            firestore.collection("quiz").document("Categories")
                                    .update(catDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(context,"Category Name Changed Successfully",Toast.LENGTH_SHORT).show();
                                            ManageCategories.manage_cat_list.get(pos).setName(catNewName);
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

    }
}
