package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SucessActivity extends AppCompatActivity {

    private TextView score;
    private CardView done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_popup);

        score = findViewById(R.id.score);
        done = findViewById(R.id.donebtn);

        String SCORE = getIntent().getStringExtra("score");
        score.setText(SCORE);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SucessActivity.this,UserDashboard.class);
                startActivity(intent);
                SucessActivity.this.finish();

            }
        });

    }
}