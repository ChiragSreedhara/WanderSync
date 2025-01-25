package com.example.sprintproject.model;

public class DestinationModel {
    private String destination;
    private int duration;

    public DestinationModel() {
        this("", 0);
    }

    public DestinationModel(String destination, int duration) {
        this.destination = destination;
        this.duration = duration;
    }

    public String getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
