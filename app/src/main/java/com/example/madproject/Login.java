package com.example.madproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private EditText email,pass;
    private CardView login;
    private FirebaseAuth firebaseAuth;
    private Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loader = new Dialog(Login.this);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);


        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().isEmpty()) {
                    email.setError("Enter Email ID");
                    return;
                }
                else
                {
                    email.setError(null);
                }

                if(pass.getText().toString().isEmpty()) {
                    pass.setError("Enter Password");
                    return;
                }
                else
                {
                    pass.setError(null);
                }

                firebaseLogin();

            }
        });

        if(firebaseAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(Login.this,adminHome.class);
            startActivity(intent);
            finish();
        }
    }

    private void firebaseLogin(){

        loader.show();

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this,"Sucess",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this,adminHome.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this,"Failure",Toast.LENGTH_SHORT).show();
                        }

                        loader.dismiss();

                    }
                });
    }
}