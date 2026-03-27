package com.example.aop.Service;
import org.springframework.stereotype.Service;


@Service
public class BatteryService {
    public void checkBattery() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Battery check completed");
    }
}
