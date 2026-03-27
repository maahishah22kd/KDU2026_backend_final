package com.example.railwaybookingsystem.broker;

@FunctionalInterface
public interface MessageHandler {

    void handle(MessageEnvelope envelope) throws Exception;
}

