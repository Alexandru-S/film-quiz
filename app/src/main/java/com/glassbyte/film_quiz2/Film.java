package com.glassbyte.film_quiz2;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sorchanolan on 21/11/15.
 */

/**
 *
 */
public class Film implements Parcelable{


    public String filmName;
    public String plot;


    public String urlPoster;



    public Film() {
        this.filmName = "";
        this.plot = "";
        this.urlPoster = "";


    }

    public Film(String fn,String poster) {
        this.filmName = fn;
        this.plot = " ";             //Empty Plot
        this.urlPoster = poster;

    }

    void setFilmName(String fn) {
        filmName = fn;
    }
    String getFilmName() {
        return filmName;
    }

    void setPlot(String p) {
        plot = p;
    }
    String getPlot() {
        return plot;
    }


    public Film(Parcel in){
        //Log.d("ACTOR", "PARCEL IN");
        filmName = in.readString();
        plot = in.readString();
        urlPoster = in.readString();
    }

    /**
     *
     * @return 0
     */
    @Override
    public int describeContents(){
        return 0;
    }

    /**
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filmName);
        dest.writeString(plot);
        dest.writeString(urlPoster);
    }

    /**
     * @return new Film(in)
     * @return new Film[size]
     */
    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel in) {
            //Log.d("ACTOR","CREATE FROM PARCEL");
            return new Film(in);
        }

        public Film[] newArray(int size) {
            //Log.d("ACTOR","CREATE FROM PARCEL NEW ARRAY");
            return new Film[size];
        }
    };


}