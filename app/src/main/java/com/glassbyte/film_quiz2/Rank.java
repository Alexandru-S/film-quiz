package com.glassbyte.film_quiz2;

/**
 * Created by sorchanolan on 06/12/15.
 */
public class Rank {

    public long rankNum;
    public long score;
    public String username;

    public Rank (long rn, long s, String un) {
        rankNum = rn;
        score = s;
        username = un;
    }

    public void setRankNum(long rn) {
        rankNum = rn;
    }

    public void setScore(long s) {
        score = s;
    }

    public void setUsername(String un) {
        username = un;
    }

    public long getRankNum() {
        return rankNum;
    }

    public long getScore(){
        return score;
    }

    public String getUsername() {
        return username;
    }
}
