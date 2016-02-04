package com.glassbyte.film_quiz2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;




/**
 * Created by Darragh on 23/11/2015.
 */
public class JSONParser {

    private static String MY_API_FILMS_TOKEN = "255cbab5-aee3-4622-a24c-64a5e8c05801";
    private static String CAST_REQUEST = "http://api.myapifilms.com/imdb/idIMDB";
    private static String CAST_URL = "http://api.myapifilms.com/imdb/idIMDB?format=json&language=en-us&aka=0&business=0&seasons=0&seasonYear=0&technical=0&filter=2&exactFilter=0&limit=1&forceYear=0&trailers=0&movieTrivia=1&awards=0&moviePhotos=0&movieVideos=0&actors=1&biography=0&uniqueName=0&filmography=0&bornAndDead=0&starSign=0&actorActress=0&actorTrivia=0&similarMovies=0&adultSearch=0";
    private static String TOP_URL = "http://api.myapifilms.com/imdb/top?start=1&end=250&format=json&data=1";
    private static String AWS_URL = "http://ec2-52-30-170-25.eu-west-1.compute.amazonaws.com:8080/";
    private static String LOCAL_URL = "http://10.0.2.2:8080/";
    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArr = null;
    static String json = "";

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject getActors(String movie_name) throws IOException {
        // Making HTTP request

        movie_name = movie_name.replace(' ', '+');
        Log.d("JSONParse", "New Object - Making Request");
        HttpURLConnection c = null;
        try {
            String url = CAST_URL + "&title=" + movie_name + "&token=" + MY_API_FILMS_TOKEN;
            URL url_obj = new URL(url );
            c = (HttpURLConnection) url_obj.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            //c.setConnectTimeout(timeout);
            //c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            Writer writer = new StringWriter();

            switch (status) {
                case 200:
                case 201:
                    char[] buffer = new char[1024];
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    int n;
                    while ((n = br.read(buffer)) != -1) {
                        //sb.append(n);
                        writer.write(buffer, 0, n);
                    }


                    br.close();
                    Log.d("Response", writer.toString());
                    JSONObject j = new JSONObject(writer.toString());
                    return j;

            }




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }


        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        Log.d("JSONParse", jObj.toString());
        // return JSON String
        return jObj;

    }

    public JSONObject getTop250() throws IOException {
        // Making HTTP request

        Log.d("JSONParse", "New Object - Making Request");
        HttpURLConnection c = null;
        try {
            String url = TOP_URL + "&token=" + MY_API_FILMS_TOKEN;
            URL url_obj = new URL(url );
            c = (HttpURLConnection) url_obj.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            //c.setConnectTimeout(timeout);
            //c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            Writer writer = new StringWriter();

            switch (status) {
                case 200:
                case 201:
                    char[] buffer = new char[1024];
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    int n;
                    while ((n = br.read(buffer)) != -1) {
                        //sb.append(n);
                        writer.write(buffer, 0, n);
                    }


                    br.close();
                    Log.d("Response", writer.toString());
                    JSONObject j = new JSONObject(writer.toString());
                    return j;

            }




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }


        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        Log.d("JSONParse", jObj.toString());
        // return JSON String
        return jObj;

    }

    //Check if the Phone is connected to the NET and the API is available
    public boolean isInternetAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public JSONObject sendToServer(String username, Double points) throws IOException {
        // Making HTTP request

        username = username.replace(' ', '+');
        Integer score = Integer.valueOf((int) Math.round(points));
        Log.d("Score", String.valueOf(score));
        Log.d("JSONParse", "New Object - Making Request");
        HttpURLConnection c = null;
        try {
            String url = AWS_URL + "result?name=" + username + "&points=" + score;
            URL url_obj = new URL(url );
            c = (HttpURLConnection) url_obj.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            //c.setConnectTimeout(timeout);
            //c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            Writer writer = new StringWriter();

            switch (status) {
                case 200:
                case 201:
                    char[] buffer = new char[1024];
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    int n;
                    while ((n = br.read(buffer)) != -1) {
                        //sb.append(n);
                        writer.write(buffer, 0, n);
                    }


                    br.close();
                    Log.d("Response", writer.toString());
                    JSONObject j = new JSONObject(writer.toString());
                    return j;

            }




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }


        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        //Log.d("JSONParse", jObj.toString());
        // return JSON String
        return jObj;

    }

    public JSONArray receiveFromServer() throws IOException {
        // Making HTTP request

        Log.d("JSONParse", "New Object - Making Request");
        HttpURLConnection c = null;
        try {
            String url = AWS_URL + "topten";
            URL url_obj = new URL(url );
            c = (HttpURLConnection) url_obj.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            //c.setConnectTimeout(timeout);
            //c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            Writer writer = new StringWriter();

            switch (status) {
                case 200:
                case 201:
                    char[] buffer = new char[1024];
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    int n;
                    while ((n = br.read(buffer)) != -1) {
                        //sb.append(n);
                        writer.write(buffer, 0, n);
                    }


                    br.close();
                    Log.d("Response", writer.toString());
                    JSONArray j = new JSONArray(writer.toString());
                    return j;

            }




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }


        try {
            jArr = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        //Log.d("JSONParse", jArr.toString());
        // return JSON String
        return jArr;

    }


}