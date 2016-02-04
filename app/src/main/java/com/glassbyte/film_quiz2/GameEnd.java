package com.glassbyte.film_quiz2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * Created by sorchanolan on 21/11/15.
 */
public class GameEnd extends AppCompatActivity {

    TextView correctAnswers;
    String quizIndicator;
    double points = 0;
    int pointsInt = 0;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_game_end);

        int temp = intent.getIntExtra("NUM_CORRECT", 0);
        int temp2 = intent.getIntExtra("NUM_ROUNDS", 0);
        points = intent.getDoubleExtra("POINTS", 0);
        new DecimalFormat("##").format(points);

        quizIndicator = intent.getStringExtra("QUIZ_INDICATOR");

        String message = temp + " / " + temp2;
        pointsInt = (int) points;

        correctAnswers = (TextView) findViewById(R.id.correct_answers);
        TextView pointsText = (TextView) findViewById(R.id.point_text);
        correctAnswers.setText(message);
        pointsText.setText("0");
        animateTextView(0,pointsInt, pointsText);
        //pointsText.setText("Points: " + new DecimalFormat("##").format(points));

    }
    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 1) * count;
            final int finalCount = (int) ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(finalCount + "");
                }
            }, time);
        }
    }

    /**
     *
     * @param view
     */
    public void BeginAgain(View view) {

        Intent intent = new Intent(this, Controller.class);
        intent.putExtra("QUIZ_INDICATOR", quizIndicator);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void backToQuizzes(View view) {

        Intent intent = new Intent(this, Intro.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void highScoresActivity(View view) {

        Intent intent = new Intent(this, HighScores.class);
        intent.putExtra("POINTS", points);
        startActivity(intent);
    }

    public void shareResult(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I just scored " + pointsInt + " points in FilmQ! Can you beat me?");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}