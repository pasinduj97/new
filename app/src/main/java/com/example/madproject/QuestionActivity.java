package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private CardView option1,option2,option3,option4;
    private TextView ans1,ans2,ans3,ans4;
    private Toolbar toolbar;
    private List<Question> questionList;
    private int quesnum;
    private CountDownTimer countDownTimer;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question);
        option1 = findViewById(R.id.opt1);
        option2 = findViewById(R.id.opt2);
        option3 = findViewById(R.id.opt3);
        option4 = findViewById(R.id.opt4);

        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);

        toolbar = findViewById(R.id.question_toolbar);
        setSupportActionBar(toolbar);



        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        getQuestionList();
        score = 0;



    }

    private void getQuestionList(){

        questionList = new ArrayList<>();

        questionList.add(new Question("question1","A","B","C","D",2));
        questionList.add(new Question("question2","A","B","C","D",3));
        questionList.add(new Question("question3","A","B","C","D",1));
        questionList.add(new Question("question4","A","B","C","D",4));

        setQuestion();

    }

    private void setQuestion(){

        getSupportActionBar().setSubtitle(String.valueOf(10));

        question.setText(questionList.get(0).getQuestion());
        ans1.setText(questionList.get(0).getOption1());
        ans2.setText(questionList.get(0).getOption2());
        ans3.setText(questionList.get(0).getOption3());
        ans4.setText(questionList.get(0).getOption4());

        getSupportActionBar().setTitle(String.valueOf(1) + "/" + String.valueOf(questionList.size()));

        startTimer();
        quesnum = 0;

    }

    private void startTimer(){

        countDownTimer = new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millis) {

                if(millis<10000)
                    getSupportActionBar().setSubtitle(String.valueOf(millis / 1000));

            }

            @Override
            public void onFinish() {

                changeQuestion();

            }
        };

        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {

        int option = 0;


        switch (view.getId()){

            case R.id.opt1:
                option = 1;
                break;

            case R.id.opt2:
                option = 2;
                break;

            case R.id.opt3:
                option = 3;
                break;

            case R.id.opt4:
                option = 4;
                break;
        }

        countDownTimer.cancel();
        checkAnswer(option,view);

    }

    private void checkAnswer(int option,View view){

        if(option == questionList.get(quesnum).getCorrectoption()){

            ((CardView)view).setCardBackgroundColor(getColor(R.color.navheadcol));
            score++;

        }
        else {

            ((CardView)view).setCardBackgroundColor(getColor(R.color.categorytextcolor));

            switch (questionList.get(quesnum).getCorrectoption()){

                case 1:
                    option1.setCardBackgroundColor(getColor(R.color.navheadcol));
                    break;
                case 2:
                    option2.setCardBackgroundColor(getColor(R.color.navheadcol));
                    break;

                case 3:
                    option3.setCardBackgroundColor(getColor(R.color.navheadcol));
                    break;
                case 4:
                    option4.setCardBackgroundColor(getColor(R.color.navheadcol));
                    break;

            }

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                changeQuestion();

            }
        },2000);


    }





    private void changeQuestion(){

        if( quesnum < questionList.size() - 1 ){


            quesnum++;
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            getSupportActionBar().setTitle(String.valueOf(quesnum+1) + "/" + String.valueOf(questionList.size()));
            getSupportActionBar().setSubtitle(String.valueOf(10));
            startTimer();


        }
        else {

            Intent intent = new Intent(QuestionActivity.this,SucessActivity.class);
            intent.putExtra("score",String.valueOf(score)+"/"+String.valueOf(questionList.size()));
            startActivity(intent);
            QuestionActivity.this.finish();

        }

    }

    private void playAnim(final View view, final int value, final int viewNum){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        if (value==0){

                            switch (viewNum){

                                case 0:
                                question.setText(questionList.get(quesnum).getQuestion());
                                break;

                                case 1:
                                ans1.setText(questionList.get(quesnum).getOption1());
                                break;

                                case 2:
                                ans2.setText(questionList.get(quesnum).getOption2());
                                break;

                                case 3:
                                ans3.setText(questionList.get(quesnum).getOption3());
                                break;

                                case 4:
                                ans4.setText(questionList.get(quesnum).getOption4());
                                break;
                            }

                            if(viewNum != 0){

                                ((CardView)view).setCardBackgroundColor(getColor(R.color.categorytextcolor));
                            }

                            playAnim(view,1,viewNum);


                            //startTimer();

                            
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}