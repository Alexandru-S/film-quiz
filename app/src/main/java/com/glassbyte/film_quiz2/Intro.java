package com.glassbyte.film_quiz2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
//import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.net.URL;


import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import com.nispok.snackbar.listeners.ActionClickListener;



public class Intro extends AppCompatActivity {

    String movie_name = "";
    ListView lv;

    private static final String TAG_RANK = "film_rank";
    private static final String TAG_NAME = "film_name";

    ArrayList<HashMap<String, String>> movieList = new ArrayList<HashMap<String, String>>();
    ArrayList<Actor> actor_list = new ArrayList<Actor>();
    ArrayList<Film> filmList = new ArrayList<Film>();


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private static final String URL = "http://api.myapifilms.com/imdb/top?start=1&end=250&token=255cbab5-aee3-4622-a24c-64a5e8c05801&format=json&data=0";
    private String name, image, price;
    private MyTask task;
    private HttpURLConnection urlConnection;
    private URL url;
    private List<MySqlDBList> customList;
    private ProgressDialog progressDialog;
    private MySqlDBHelper adapter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Film Quiz");
        setSupportActionBar(toolbar);


        //Fill the List View With Movie Titles and Their Rank   // This will eventually be filled from the DB

        recyclerView = (RecyclerView)findViewById(R.id.recyclerList);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Intro.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();

        if (connected){
            accessWebService();
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    accessWebService();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }else {
            com.nispok.snackbar.Snackbar.with(Intro.this).text("No Internet Connection").actionLabel("Retry").actionListener(new ActionClickListener() {
                @Override
                public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                    accessWebService();
                    snackbar.dismiss();
                }
            }).show(Intro.this);
        }

        recyclerView.addOnItemTouchListener(new MySqlDB(Intro.this, new MySqlDB.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(Intro.this, "Item " + customList.get(position).getName() + " Clicked", Toast.LENGTH_LONG).show();
                new getActors().execute(customList.get(position).getName());
            }
        }));

    }


    private class MyTask extends AsyncTask<String, Void, List>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Intro.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                urlConnection.setConnectTimeout(5000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder jsonResult = inputStreamToString(in);
                customList = new ArrayList<>();
                JSONObject jsonResponseObject = new JSONObject(jsonResult.toString());
                /***/
                //JSONArray jsonResponseArray = new JSONArray(jsonResult.toString());
                // [  for JSONArray
                // { for JSONObject

                JSONObject jsonMasterNode = jsonResponseObject.optJSONObject("data");
                jsonMasterNode.getString("movies");
                System.out.println("MASTER NODE " + jsonMasterNode);

                JSONArray jsonMainNode = jsonMasterNode.getJSONArray("movies");
                System.out.println("LENGTH " + jsonMainNode.length());
                System.out.println(jsonMainNode);

                for(int i = 0; i < jsonMainNode.length(); i++){
                    //System.out.println("IN ARRAY " + jsonMainNode.get(i));
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    name = jsonChildNode.optString("title");
                    price = jsonChildNode.optString("rank");
                    image = jsonChildNode.optString("urlPoster");
                    customList.add(new MySqlDBList(name, price, image));


                    filmList.add(new Film(name.toString(), image.toString()));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return customList;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = " ";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (Exception e) {
                Intro.this.finish();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(List list) {
            ListDrawer(list);
            progressDialog.dismiss();
        }
    }
    public void ListDrawer(List<MySqlDBList> customList) {
        adapter = new MySqlDBHelper(customList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void accessWebService(){
        task = new MyTask();
        task.execute(new String[] {URL});
    }


    private class getPlotsFromAPI extends AsyncTask<String, Void, JSONObject> {
        JSONParser jparse = new JSONParser();
        JSONObject j = null;
        ProgressDialog progress = new ProgressDialog(Intro.this);
        JSONObject result_obj = null;

        @Override
        protected void onPreExecute() {
            Log.d("intro", "Pre Execute");

            findViewById(R.id.loadingFrame).setVisibility(View.VISIBLE);
        }

        /**
         *
         * @param params
         * @return
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            Log.d("intro", "Doing In BG");
            try {
                if(jparse.isInternetAvailable(getApplicationContext())){
                    result_obj = jparse.getTop250();

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "We cannot reach the network. Please check your connection.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("Result Ob", j.toString());
            return result_obj;
        }

        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(JSONObject result) {
            try {

                if(result_obj != null) {
                    JSONObject datas_obj = (result_obj.getJSONObject("data"));
                    JSONArray movie_arr = datas_obj.getJSONArray("movies");
                    for(int i =0; i< movie_arr.length(); i++){
                        JSONObject movie_obj = new JSONObject(movie_arr.get(i).toString());
                        String plot = movie_obj.getString("plot");
                        Log.d("PLOT", plot);
                        filmList.get(i).setPlot(plot);
                    }



                    //Dismiss the progress bar
                    findViewById(R.id.loadingFrame).setVisibility(View.INVISIBLE);
                    //Move onto the quiz
                    BeginPlot();
                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "We cannot reach the network. Please check your connection.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    //progress.dismiss();
                    findViewById(R.id.loadingFrame).setVisibility(View.INVISIBLE);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

        /**
     *
     */
    private class getActors extends AsyncTask<String, Void, JSONObject> {
        JSONParser jparse = new JSONParser();
        JSONObject j = null;
        ProgressDialog progress = new ProgressDialog(Intro.this);
        JSONObject result_obj = null;

        @Override
        protected void onPreExecute() {
            Log.d("intro", "Pre Execute");

            findViewById(R.id.loadingFrame).setVisibility(View.VISIBLE);



            //progress.setTitle("Loading");
            //progress.setMessage("Loading Quiz...");
            //progress.show();
        }


        /**
         *
         * @param params
         * @return
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            movie_name = params[0];

            Log.d("intro", "Doing In BG");
            try {
                if(jparse.isInternetAvailable(getApplicationContext())){
                    result_obj = jparse.getActors(movie_name);

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "We cannot reach the network. Please check your connection.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("Result Ob", j.toString());
            return result_obj;
        }

        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(JSONObject result) {

            try {

                if(result_obj != null) {
                    JSONObject datas_obj = (result_obj.getJSONObject("data"));
                    JSONArray movie_arr = datas_obj.getJSONArray("movies");
                    JSONObject movie_obj = new JSONObject(movie_arr.get(0).toString());
                    JSONArray actors_arr = movie_obj.getJSONArray("actors");
                    //Log.d("JSON ACTORS", actors_arr.get(0).toString());

                    for (int i = 0; i < actors_arr.length(); i++) {
                        JSONObject actor_obj = actors_arr.getJSONObject(i);
                        //Log.d("Actor Name", actor_obj.getString("actorName"));
                        //String name = childJSONObject.getString("name");
                        //int age     = childJSONObject.getInt("age");

                        Actor a = new Actor(
                                actor_obj.getString("actorName"),      //Actor Name
                                " ",                                    //Movie Title
                                actor_obj.getString("urlProfile"),     //Profile URL
                                actor_obj.getString("urlPhoto"),       //Profile Picture
                                actor_obj.getString("actorId"),        //Actor ID
                                actor_obj.getString("character"));    //Main Character?

                        actor_list.add(a);
                    }

                    //Dismiss the progress bar
                    //progress.dismiss();
                    findViewById(R.id.loadingFrame).setVisibility(View.INVISIBLE);

                    //Move onto the quiz
                    BeginActorQuiz();
                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "We cannot reach the network. Please check your connection.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    //progress.dismiss();
                    findViewById(R.id.loadingFrame).setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void getPlots(View view){
        new getPlotsFromAPI().execute();
    }

    public void BeginPlot () {
        Intent intent = new Intent(this, Controller.class);
        intent.putExtra("QUIZ_INDICATOR", "plot");
        intent.putParcelableArrayListExtra("FILM_ARRAY",filmList);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void GetActors (View view ) {

        //Call Async Task - Get Movie List In the background
        Log.d("intro", "Calling Actors");
        Random rn = new Random();
        new getActors().execute(customList.get(rn.nextInt(customList.size())).name);
    }

    /**
     *
     */
    public void BeginActorQuiz( ){
        Intent intent = new Intent(this, Controller.class);
        intent.putExtra("QUIZ_INDICATOR", "actor");
        intent.putExtra("MOVIE_NAME", movie_name);
        //Feed in the actor List
        intent.putParcelableArrayListExtra("ACTOR_ARRAY", actor_list);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.info_menu_item) {
            Intent intent = new Intent(this, InformationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}