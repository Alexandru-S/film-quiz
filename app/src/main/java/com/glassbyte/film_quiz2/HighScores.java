package com.glassbyte.film_quiz2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sorchanolan on 04/12/15.
 */
public class HighScores extends AppCompatActivity implements View.OnClickListener {


    double highestPoints = 0;
    String fileName = "highScores";
    TextView myHighScore;
    ListView rankings;
    String username = "";
    FloatingActionButton clear;
    Rank rankArray[] = new Rank[10];
    ArrayList<HashMap<String, String>> rankList = new ArrayList<HashMap<String, String>>();


    private static final String TAG_RANK = "rank_num";
    private static final String TAG_NAME = "username";
    private static final String TAG_SCORE = "score";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_high_scores);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        double newPoints = intent.getDoubleExtra("POINTS", 0);
        myHighScore = (TextView) findViewById(R.id.my_highscore);
        rankings = (ListView) findViewById(R.id.rankings);
        clear =  (FloatingActionButton) findViewById(R.id.clear);

        for (int i = 0; i < 10; i++) {
            rankArray[i] = new Rank(0, 0, "");
        }



        highestPoints = readScore();

        if (newPoints > highestPoints) {
            highestPoints = newPoints;

            newHighScore(highestPoints);
        }

        else new getServerInfo().execute("", "");

        writeToFile(highestPoints);

        myHighScore.setText(String.valueOf(highestPoints));




        clear.setOnClickListener(this);

    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        highestPoints = 0;
        myHighScore.setText(String.valueOf(highestPoints));

        writeToFile(highestPoints);

    }

    private void newHighScore(double highestPoints) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You got a new high score! Please enter your name:");

        // Set up the input
        final EditText input = new EditText(this);
        final Double score = highestPoints;
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = input.getText().toString();

                Log.d("USERNAME", username);
                new getServerInfo().execute(username, String.valueOf(score));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                new getServerInfo().execute("", "");
            }
        });

        builder.show();



    }


    private void writeToFile(double highestPoints) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(String.valueOf(highestPoints));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private Double readScore() {

        Double ret = 0.0;

        try {
            InputStream inputStream = openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String r = stringBuilder.toString();
                ret = Double.parseDouble(r);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("main activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("main activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    /**
     *
     */
    private class getServerInfo extends AsyncTask<String, Void, JSONArray> {  //server class ///////////////////////////////////////////////

        JSONParser jparse = new JSONParser();
        JSONArray j = null;
        ProgressDialog progress = new ProgressDialog(HighScores.this);
        JSONArray result_arr = null;
        JSONObject result_obj = null;

        /**
         *
         */
        @Override
        protected void onPreExecute() {
            Log.d("intro", "Pre Execute");

            clear.setVisibility(View.INVISIBLE);

            findViewById(R.id.loadingFrame).setVisibility(View.VISIBLE);

            //progress.setTitle("Loading");
            //progress.setMessage("Loading high scores...");
            //progress.show();
        }

        /**
         *
         * @param params
         * @return
         */
        @Override
        protected JSONArray doInBackground(String... params) {

            String uname = params[0];
            String _points = params[1];
            Log.d("points", _points);
            Log.d("intro", "Doing In BG");
            try {
                if(jparse.isInternetAvailable(getApplicationContext())){

                    if(_points != "")
                    {
                        result_obj = jparse.sendToServer(uname, Double.parseDouble(_points));
                        result_arr = jparse.receiveFromServer();
                    }

                    else result_arr = jparse.receiveFromServer();

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "We cannot reach the network. Please check your connection.(1)", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("Result Ob", j.toString());
            return result_arr;
        }

        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(JSONArray result) {

            try {

                if(result_arr != null) {

                    for (int i = 0; i < result_arr.length(); i++) {
                        JSONObject rank_obj = result_arr.getJSONObject(i);

                        Rank a = new Rank(i+1, rank_obj.getLong("points"), rank_obj.getString("name"));

                        rankArray[i] = a;
                    }

                    findViewById(R.id.loadingFrame).setVisibility(View.INVISIBLE);
                    clear.setVisibility(View.VISIBLE);
                    //Dismiss the progress bar

                    progress.dismiss();

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "We cannot reach the network. Please check your connection.(2)", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    progress.dismiss();

                    findViewById(R.id.loadingFrame).setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d("Actors", actors.toString());

            for(int i = 0; i< rankArray.length; i++){
                HashMap<String, String> rank_map = new HashMap<String, String>();
                rank_map.put(TAG_RANK, Integer.toString(i+1));
                rank_map.put(TAG_NAME, rankArray[i].getUsername());
                rank_map.put(TAG_SCORE, String.valueOf(rankArray[i].getScore()));
                Log.d("RANK ARR", rankArray[i].getUsername());
                Log.d("RANK MAP", rank_map.toString());
                rankList.add(rank_map);
            }

            //Set the List View with the Movies
            ListAdapter adapter = new SimpleAdapter(HighScores.this, rankList,
                    R.layout.rank_list_layout,
                    new String[] { TAG_RANK,TAG_NAME, TAG_SCORE }, new int[] {
                    R.id.rank_num, R.id.username, R.id.score});

            rankings.setAdapter(adapter);

        }



        //Log.d("Results on Post", result_obj.toString());

    }

}
