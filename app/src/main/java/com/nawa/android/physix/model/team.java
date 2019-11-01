package com.nawa.android.physix.model;

import java.util.HashMap;

public class team {
    private String team , FM , FoM , SM , TM , code;
    private HashMap<String,Object> Leader;
    private boolean answered;
    private int score ;

    public team(String team, String FM, String foM, String SM, String TM, String code, HashMap<String, Object> leader, boolean answered, int score) {
        this.team = team;
        this.FM = FM;
        FoM = foM;
        this.SM = SM;
        this.TM = TM;
        this.code = code;
        Leader = leader;
        this.answered = answered;
        this.score = score;
    }

    public String getCode() {
        return code;
    }

    public String getTeam() {
        return team;
    }

    public String getFM() {
        return FM;
    }

    public String getFoM() {
        return FoM;
    }

    public String getSM() {
        return SM;
    }

    public String getTM() {
        return TM;
    }

    public HashMap<String, Object> getLeader() {
        return Leader;
    }

    public boolean isAnswered() {
        return answered;
    }

    public int getScore() {
        return score;
    }

    public team() {

    }
}
