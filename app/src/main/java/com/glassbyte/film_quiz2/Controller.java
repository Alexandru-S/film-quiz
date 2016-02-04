package com.glassbyte.film_quiz2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sorchanolan on 21/11/15.
 */



public class Controller extends AppCompatActivity implements View.OnClickListener {


    int counter = 0;     //for counting number of rounds
    int numberOfRounds;     //when counter = this, end game
    int correctAnswers = 0;
    long timeLeft = 16000;
    double points = 0;
    boolean answerClicked = false;      //This prevents Crashing When the animations are running
    String quizIndicator;
    String movie_name;
    TextView question1;
    TextView correctCounter;
    TextView countdown;
    TextView filmName;
    View v;
    Button answers[] = new Button[4];
    ArrayList<Film> filmArray = new ArrayList<>();
    List<String> options = new ArrayList<>();     //list of 4 potential answers that populate the buttons
    List<String> optionsList = new ArrayList<>();    //lists useful for shuffling, can probably get rid of array and just use list if easier
    List<String> questionList = new ArrayList<>();
    List<Film> pick50 = new ArrayList<>();
    CountDownTimer countDownTimer;
    ArrayList<Actor> actor_list = null;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_main);

        //initialising buttons and text to be displayed
        question1 = (TextView) findViewById(R.id.question);
        correctCounter = (TextView) findViewById(R.id.correct_counter);
        countdown = (TextView) findViewById(R.id.countdown);
        filmName = (TextView) findViewById(R.id.film_name);
        answers[0] = (Button) findViewById(R.id.ans1);
        answers[1] = (Button) findViewById(R.id.ans2);
        answers[2] = (Button) findViewById(R.id.ans3);
        answers[3] = (Button) findViewById(R.id.ans4);


        movie_name = intent.getStringExtra("MOVIE_NAME");
        quizIndicator = intent.getStringExtra("QUIZ_INDICATOR");

        Bundle data = getIntent().getExtras();

        switch (quizIndicator) {     //check which type of quiz they clicked
            case "plot":
                //API feed here

                filmArray = data.getParcelableArrayList("FILM_ARRAY");
                //Shuffle the Movies into a random order
                Collections.shuffle(filmArray);
/*
                for (int i = 0; i < 50; i++) {
                    pick50.add(filmArray.get(i));
                }
*/
                //Set the Number of Rounds
                numberOfRounds = 10;
                //enters array elements into a list so they can be easily shuffled each time a new question is asked
                for (int i = 0; i < filmArray.size(); i++) {
                    //Add the Film Names into a list of possible Answers
                    optionsList.add(filmArray.get(i).getFilmName());      //Film Name
                    //Add the Film Plots into a list of Possible Plots
                    questionList.add(filmArray.get(i).getPlot());         //Film Plots

                    //Note that the films and their plots were added in the same order
                }
                break;
            case "actor":

                actor_list = data.getParcelableArrayList("ACTOR_ARRAY");

                //Shuffle The Actors
                Collections.shuffle(actor_list);
                //Set the Number of Rounds
                numberOfRounds = actor_list.size();

                for (int i = 0; i < actor_list.size(); i++) {
                    //Add the Actor Names into a list of possible Answers
                    optionsList.add(actor_list.get(i).actorName);           //Actor Name
                    //Add the Character Names  into a list of Possible Questions
                    questionList.add(actor_list.get(i).character);         //Character Name

                    //Note that the films and their plots were added in the same order
                }

                question1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);


                break;
        }

        //And run the quiz
        question();

        //listens out for when any of the buttons are clicked to move on to the next question
        for (int i = 0; i < 4; i++) {
            answers[i].setOnClickListener(this);
        }

    }

    /**
     *
     */
    private void question() {

        answerClicked = false;      //This prevents Crashing When the animations are running


        //Update the Number of Correct answers
        String correctAnswersString = correctAnswers + "/" + (counter + 1);
        correctCounter.setText(correctAnswersString);
        filmName.setText(movie_name);

        countDownTimer = new CountDownTimer(timeLeft, 100) {

            public void onTick(long millisUntilFinished) {
                double secondsLeft = millisUntilFinished / 1000.0;

                countdown.setText(new DecimalFormat("##").format(secondsLeft));

                timeLeft = millisUntilFinished;

            }

            public void onFinish() {
                endGame();

            }
        }.start();

        //Current Options is a list of Answer Options that doesn't overwrite the original order
        List<String> currentOptions = new ArrayList<>();
        //Popuplate The Current Options Array
        for (int i = 0; i < optionsList.size(); i++) {
            currentOptions.add(optionsList.get(i));
        }

        //sets the question of the first list of questions
        question1.setText(questionList.get(counter));


        //then sets the name of that film as one of the options
        options.add(currentOptions.get(counter));

        //this gets rid of the above film name from the list to avoid having the same film name more than once as an option
        for (int j = 0; j < currentOptions.size(); j++) {

            if (currentOptions.get(j).equals(optionsList.get(counter))) {
                currentOptions.remove(j);
            }
        }

        //now shuffles the list each time to get different potential answers
        Collections.shuffle(currentOptions);

        //chooses the top three from the shuffled list to be added to the options list
        for (int k = 0; k < 3; k++) {
            options.add(currentOptions.get(k));
        }

        //now shuffle the clickable options that so that the correct answer isn't always the first one
        Collections.shuffle(options);

        //set these options as text on the four buttons
        for (int l = 0; l < 4; l++) {
            answers[l].setText(options.get(l));
        }

        //Increase the Question Count
        counter++;


    }

    /**
     *
     */


    private void endGame() {
        Intent intent = new Intent(this, GameEnd.class);

        timeLeft = timeLeft / 100;
        timeLeft = (timeLeft * 10) / numberOfRounds;
        points = timeLeft * correctAnswers;

        //pass through the number of correct answers and number of rounds in to display on the next screen
        intent.putExtra("NUM_CORRECT", correctAnswers);
        intent.putExtra("NUM_ROUNDS", numberOfRounds);
        intent.putExtra("QUIZ_INDICATOR", quizIndicator);
        intent.putExtra("POINTS", points);
        startActivity(intent);
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {


        if (!answerClicked) {

            answerClicked = true;

            countDownTimer.cancel();
            movie_name = " ";
            boolean correct = false;
            int buttonPressed = 0;
            int correctButton = 0;

            //switch statements to check which is the right answer and increment correctAnswers variable if correct
            switch (quizIndicator) {
                case "plot":
                    switch (v.getId()) {

                        case R.id.ans1:
                            if (options.get(0).equals(filmArray.get(counter - 1).getFilmName())) {
                                correct = true;
                            }
                            buttonPressed = 0;
                            break;
                        case R.id.ans2:
                            if (options.get(1).equals(filmArray.get(counter - 1).getFilmName())) {
                                correct = true;
                            }
                            buttonPressed = 1;
                            break;
                        case R.id.ans3:
                            if (options.get(2).equals(filmArray.get(counter - 1).getFilmName())) {
                                correct = true;
                            }
                            buttonPressed = 2;
                            break;
                        case R.id.ans4:
                            if (options.get(3).equals(filmArray.get(counter - 1).getFilmName())) {
                                correct = true;
                            }
                            buttonPressed = 3;
                            break;
                        default:
                            break;
                    }

                    //For Loop to Get Correct Answer
                    for (int i = 0; i < 4; i++) {
                        if (options.get(i).equals(filmArray.get(counter - 1).getFilmName())) {
                            correctButton = i;
                        }
                    }
                    break;
                case "actor":
                    switch (v.getId()) {
                        case R.id.ans1:
                            if (options.get(0).equals(actor_list.get(counter - 1).actorName)) {
                                correct = true;
                            }
                            buttonPressed = 0;
                            break;
                        case R.id.ans2:
                            if (options.get(1).equals(actor_list.get(counter - 1).actorName)) {
                                correct = true;
                            }
                            buttonPressed = 1;
                            break;
                        case R.id.ans3:
                            if (options.get(2).equals(actor_list.get(counter - 1).actorName)) {
                                correct = true;
                            }
                            buttonPressed = 2;
                            break;
                        case R.id.ans4:
                            if (options.get(3).equals(actor_list.get(counter - 1).actorName)) {
                                correct = true;
                            }
                            buttonPressed = 3;
                            break;
                        default:
                            break;
                    }
                    //For Loop to Get Correct Answer
                    for (int i = 0; i < 4; i++) {
                        if (options.get(i).equals(actor_list.get(counter - 1).actorName)) {
                            correctButton = i;
                        }
                    }
                    break;
            }
            if (correct) {
                correctAnswers++;

                //Changes answer text green if correct
                answers[buttonPressed].setBackgroundColor(getResources().getColor(R.color.green));

                DelayQuestion delay = new DelayQuestion(buttonPressed, correctButton);
                Handler myHandler = new Handler();
                myHandler.postDelayed(delay, 500); //delay colour change back to text colour for 500ms


                timeLeft = timeLeft + 5500;
            } else { //If  answer clicked is incorrect
                //Turn Answer Red

                answers[buttonPressed].setBackgroundColor(getResources().getColor(R.color.red));
                //answers[correctButton].setBackgroundColor(getResources().getColor(R.color.green));

                DelayQuestion delay = new DelayQuestion(buttonPressed, correctButton);
                Handler myHandler = new Handler();
                myHandler.postDelayed(delay, 750);


                DelayCorrectAnswer delayAnswer = new DelayCorrectAnswer(correctButton);
                Handler myNewHandler = new Handler();
                myNewHandler.postDelayed(delayAnswer, 400); //delay colour change  for 250ms

                timeLeft = timeLeft + 500;

            }

            //get rid of previous options for next round
            options.clear();

            if (counter == numberOfRounds) endGame();
                //else question();
            else {
                Handler myHandler = new Handler();
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        question();
                    }
                }, 750);
            }


        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private class DelayQuestion implements Runnable {
        private int bp; //button pressed
        private int cb; //correctbutton


        public DelayQuestion(int _bp, int _cb) {
            this.bp = _bp;
            this.cb = _cb;
        }

        public void run() {
            answers[bp].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //change button text back to original
            answers[cb].setBackgroundColor(getResources().getColor(R.color.colorPrimary));//after 500 ms
        }

    }


    private class DelayCorrectAnswer implements Runnable {
        private int cb; //correctbutton


        public DelayCorrectAnswer(int _cb) {
            this.cb = _cb;
        }

        public void run() {

            answers[cb].setBackgroundColor(getResources().getColor(R.color.green));
        }

    }

}
