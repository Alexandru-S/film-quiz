package com.glassbyte.film_quiz2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Darragh on 23/11/2015.
 */

/**
 *
 */
public class Actor implements Parcelable{
    public String actorName;
    public String movieTitle;
    public String urlProfile;
    public String urlPhoto;
    public String actorID;
    public String character;
    //private boolean main;

    /**
     *
     * @param _actorName
     * @param _movieTitle
     * @param _urlProfile
     * @param _urlPhoto
     * @param _actorID
     * @param _character
     */
    public Actor(String _actorName, String _movieTitle, String _urlProfile, String _urlPhoto, String _actorID, String _character){
        actorName = _actorName;
        movieTitle = _movieTitle;
        urlProfile = _urlProfile;
        urlPhoto = _urlPhoto;
        actorID = _actorID;
        character = _character;
        //main = _main;

    }

    /**
     *
     * @param in
     */
    public Actor(Parcel in){
        //Log.d("ACTOR", "PARCEL IN");
        actorName = in.readString();
        movieTitle = in.readString();
        urlProfile = in.readString();
        urlPhoto = in.readString();
        actorID = in.readString();
        character = in.readString();
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
        //Log.d("ACTOR", "WRITE TO PARCEL");
        dest.writeString(actorName);
        dest.writeString(movieTitle);
        dest.writeString(urlProfile);
        dest.writeString(urlPhoto);
        dest.writeString(actorID);
        dest.writeString(character);

    }

    /**
     * @return new Actor(in)
     * @return new Actor[size]
     */
    public static final Parcelable.Creator<Actor> CREATOR = new Parcelable.Creator<Actor>() {
        public Actor createFromParcel(Parcel in) {
            //Log.d("ACTOR","CREATE FROM PARCEL");
            return new Actor(in);
        }

        public Actor[] newArray(int size) {
            //Log.d("ACTOR","CREATE FROM PARCEL NEW ARRAY");
            return new Actor[size];
        }
    };


}