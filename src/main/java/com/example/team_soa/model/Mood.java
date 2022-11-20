package com.example.team_soa.model;

public enum Mood {
    SORROW("sorrow"),
    APATHY("apathy"),
    FRENZY("frenzy");

    private final String mood;

    private Mood(String mood) {
        this.mood = mood;
    }

    public String getMood() {
        return mood;
    }
}