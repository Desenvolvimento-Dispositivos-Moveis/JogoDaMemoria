package com.example.jogodamemoria.model;
public class ScoreEntry implements  Comparable<ScoreEntry>{

    private final long timeInMillis;
    private final int attempts;

    public ScoreEntry(long timeInMillis, int attempts) {
        this.timeInMillis = timeInMillis;
        this.attempts = attempts;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public int getAttempts() {
        return attempts;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Long.compare(this.timeInMillis, other.timeInMillis);
    }
}
