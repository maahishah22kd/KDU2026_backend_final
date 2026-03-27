package com.example.aop.Exception;

public class HardwareFailureException extends RuntimeException{
    public HardwareFailureException(){
        super("Look there is a Hardware Failure!");
    }
}
