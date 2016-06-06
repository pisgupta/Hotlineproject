package com.jaskirat.event.tracker.com.jaskirat.event.preference;

/**
 * Created by jass on 4/10/2016.
 */
public interface MyConstantAPI {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String API_KEY = "&key=AIzaSyB6Pir-AU1UaYDh_BpwtB3m7rDG0X4xzxY";
    public static final String ORIGIN = "origin=";
    public static final String DESTINATION = "&destination=";

    public static final String VOID = "&avoid=highways";
    public static final String MODE = "&mode=DRIVING";
}
