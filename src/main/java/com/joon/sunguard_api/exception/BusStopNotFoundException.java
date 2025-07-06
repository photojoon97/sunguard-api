package com.joon.sunguard_api.exception;

public class BusStopNotFoundException extends RuntimeException{
    public BusStopNotFoundException(String message){
        super(message);
    }
}
